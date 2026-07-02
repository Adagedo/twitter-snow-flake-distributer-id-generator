package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import code.adagedo.distributed_id_generator_twitter_snow_flake.engine.IdGeneratorEngine;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService extends ComputeIdGrpc.ComputeIdImplBase {

    public void generateId(ServerInfoRequest request, StreamObserver<code.adagedo.commonproto.ServerResponse> observer){

        String server_id = request.getServerId();
        String data_center_id = request.getDataCenterId();

        IdGeneratorEngine engine = new IdGeneratorEngine(data_center_id, server_id);
        long id = engine.generate_id();
        ServerResponse response = ServerResponse.newBuilder().setSnowFlakeId(id).build();
        observer.onNext(response);
        observer.onCompleted();
    }
}
