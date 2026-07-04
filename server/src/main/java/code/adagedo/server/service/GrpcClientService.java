package code.adagedo.server.service;

import code.adagedo.commonproto.ComputeIdGrpc;
import code.adagedo.commonproto.ServerInfoRequest;
import code.adagedo.commonproto.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrpcClientService {

    private final ComputeIdGrpc.ComputeIdBlockingStub blockingStub;

    public long getSnowFlakeId() {
        ServerInfoRequest request = ServerInfoRequest.newBuilder()
                .setDatacenterName("us-east").setServerName("server-one").build();
        ServerResponse response = blockingStub.generateId(request);
        return response.getSnowFlakeId();
    }
}
