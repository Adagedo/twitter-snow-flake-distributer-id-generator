package code.adagedo.distributed_id_generator_twitter_snow_flake.engine;

import code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions.InvalidSystemClockException;
import code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions.InvalidUserServiceError;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.regex.Pattern;

@Data
@Slf4j
public class IdGeneratorEngine {

    private static final long TWITTER_EPOCH = 1288834974657L;

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final Pattern AGENT_PATTERN = Pattern.compile("^([a-zA-Z][a-zA-Z\\-0-9]*)$");

    private final long serverId;
    private final long datacenterId;
    private final Random rand;

    private final String serverName;
    private final String datacenterName;

    private long sequence;
    private long lastTimestamp = -1L;


    public IdGeneratorEngine(long serverId, long datacenterId, String serverName, String datacenterName){
        this(serverId, datacenterId, datacenterName, serverName,0L);
    }

    public IdGeneratorEngine(long serverId, long datacenterId, String serverName, String datacenterName, long sequence){
        this.serverId = serverId;
        this.datacenterId = datacenterId;
        this.serverName = serverName;
        this.datacenterName = datacenterName;
        this.rand = new Random();
        this.sequence = sequence;

        if(serverId > MAX_WORKER_ID || serverId < 0) throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));

        if(datacenterId > MAX_DATACENTER_ID || datacenterId < 0) throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));

        log.info("engine starting. timestamp left shift {}, datacenter id bits {}, worker id bits {}, sequence bits {}, serverId {}",
                TIMESTAMP_LEFT_SHIFT, DATACENTER_ID_BITS, WORKER_ID_BITS, SEQUENCE_BITS, serverId);
    }

    public boolean validUserService(String serviceName){
        if(serviceName == null) return false;
        return AGENT_PATTERN.matcher(serviceName).matches();
    }

    @PostConstruct
    void publishAudit(){
        // logic
    }

    public long get_server_id(){ return serverId; }

    public long get_datacenter_id() { return datacenterId; }

    public long get_timestamp() { return System.currentTimeMillis(); }


    public synchronized long nextId(){

        long timestamp = timeGen();

        if(timestamp < lastTimestamp){
            // incrementation exceptions goes here
            log.info("clock is moving backword. Rejecting request until {}", timestamp);
            throw new InvalidSystemClockException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
            );
        }

        if(lastTimestamp == timestamp){
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0){
                timestamp = tillNextMillis(lastTimestamp);
            }
        }else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;


        return ((timestamp - TWITTER_EPOCH) << TIMESTAMP_LEFT_SHIFT) |
                (datacenterId << DATACENTER_ID_SHIFT) |
                (serverId << WORKER_ID_SHIFT) |
                sequence;
    }

    protected long tillNextMillis(long lastTimestamp){
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp){
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() { return System.currentTimeMillis(); }
}
