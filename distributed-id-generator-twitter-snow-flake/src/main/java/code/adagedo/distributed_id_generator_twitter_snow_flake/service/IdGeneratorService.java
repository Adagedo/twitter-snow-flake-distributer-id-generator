package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import code.adagedo.distributed_id_generator_twitter_snow_flake.engine.IdGeneratorEngine;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;


@GrpcService
public class IdGeneratorService extends ComputeIdGrpc.ComputeIdImplBase {

    public void generateId(ServerInfoRequest request, StreamObserver<code.adagedo.commonproto.ServerResponse> observer){

        String serverName = request.getServerName();

        long id =   123456789876L;
        ServerResponse response = ServerResponse.newBuilder().setSnowFlakeId(id).build();
        observer.onNext(response);
        observer.onCompleted();
    }
}
