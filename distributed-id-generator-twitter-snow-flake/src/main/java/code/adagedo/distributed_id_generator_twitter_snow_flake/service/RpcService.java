package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import code.adagedo.distributed_id_generator_twitter_snow_flake.engine.IdGeneratorEngine;
import code.adagedo.distributed_id_generator_twitter_snow_flake.producer.AuditEventProducer;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;


@GrpcService
@Slf4j
@RequiredArgsConstructor
public class RpcService extends ComputeIdGrpc.ComputeIdImplBase {

    private final RedisServerIdRegistryImplementation redisServerIdRegistry;

    private final AuditEventProducer auditEventProducer;
    public void generateId(ServerInfoRequest request, StreamObserver<code.adagedo.commonproto.ServerResponse> observer){

        try{

            long datacenterId = redisServerIdRegistry.createDatacenterId(request.getDatacenterName());

            long serverId = redisServerIdRegistry.createServerId(request.getServerName());

            IdGeneratorEngine engine = new IdGeneratorEngine(serverId, datacenterId, request.getServerName(), request.getDatacenterName(), auditEventProducer);

            long snowFlakeId = engine.nextId();

            engine.publishAudit(snowFlakeId); // sending audits logs to Kafka topic

            ServerResponse response = ServerResponse.newBuilder().setSnowFlakeId(snowFlakeId).build();

            observer.onNext(response);
            observer.onCompleted();

        }catch (Exception exception){

            log.info(exception.getMessage());

            log.error("failed to send rpc response to server name{} and datacenter name, {}", request.getServerName(), request.getDatacenterName());
            observer.onError(io.grpc.Status.INTERNAL.withDescription(exception.getCause().getMessage()).asRuntimeException());
        }
    }
}
