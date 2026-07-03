package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import code.adagedo.distributed_id_generator_twitter_snow_flake.engine.IdGeneratorEngine;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;


@GrpcService
@Slf4j
@RequiredArgsConstructor
public class RpcService extends ComputeIdGrpc.ComputeIdImplBase {

    private final RedisServerIdRegistry redisServerIdRegistry;

    public void generateId(ServerInfoRequest request, StreamObserver<code.adagedo.commonproto.ServerResponse> observer){

        try{
            String serverName = request.getServerName();

            long datacenterId = redisServerIdRegistry.getDatacenterId();
            long serverId = redisServerIdRegistry.createServerId(serverName);
            IdGeneratorEngine engine = new IdGeneratorEngine(datacenterId, serverId);

            long snowFlakeId = engine.nextId();

            ServerResponse response = ServerResponse.newBuilder().setSnowFlakeId(snowFlakeId).build();
            observer.onNext(response);
            observer.onCompleted();
        }catch (Exception exception){
            log.error("failed to send rpc response to {}", request.getServerName());
            observer.onError(io.grpc.Status.INTERNAL.withDescription(exception.getCause().getMessage()).asRuntimeException());
        }
    }
}
