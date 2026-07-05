package code.adagedo.servertwo.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrpcClientService {

    private final ComputeIdGrpc.ComputeIdBlockingStub blockingStub;

    public long getSnowFlakeId(){
        ServerInfoRequest request = ServerInfoRequest.newBuilder().setDatacenterName("us-central").setServerName("server-two").build();
        ServerResponse response = blockingStub.generateId(request);
        return response.getSnowFlakeId();
    }
}
