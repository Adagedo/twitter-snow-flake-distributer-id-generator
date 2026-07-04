package code.adagedo.distributed_id_generator_twitter_snow_flake.producer;

import code.adagedo.distributed_id_generator_twitter_snow_flake.audit.AuditLogEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;




@Component
@Slf4j
//@RequiredArgsConstructor
public class AuditEventProducer {

    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public static final String HEADER_EVENT_SOURCE = "event-source";
    public static final String SOURCE_SCANNER = "scanner";

    public AuditEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishEvent(AuditLogEntry logEntry){
        String key = buildKey(logEntry);
        String value = objectMapper.writeValueAsString(logEntry);
        ProducerRecord<String, String> producerRecord= buildProducerRecord(key, value);
        CompletableFuture<SendResult<String, String>> compatibleFeature = kafkaTemplate.send(producerRecord);
        compatibleFeature
                .whenComplete(((SendResult, throwable) -> {
                    if (throwable != null){
                        hangleFailure(key, value, throwable);
                    }else {
                        handleSuccess(key, value, SendResult);
                    }
                }));
    }

    private String buildKey(AuditLogEntry logEntry){
        return logEntry.getDatacenterName() + ":" + logEntry.getServerName();
    }

    private void handleSuccess(String key, String value, SendResult<String, String> sendResult){
        log.info("Message sent successfully for the key: {}, value : {}, and the partition is: {}", key, value, sendResult.getRecordMetadata().partition());
    }

    private void hangleFailure(String key, String value, Throwable throwable){
        log.error("Error send the message is: {}", throwable.getMessage());
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String value){
        List<Header> recordHeaders = List.of(new RecordHeader(HEADER_EVENT_SOURCE, SOURCE_SCANNER.getBytes(StandardCharsets.UTF_8)));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

}
