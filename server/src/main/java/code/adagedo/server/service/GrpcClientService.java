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
                .setDatacenterName("my datacenter").setServerName("my server").build();
        ServerResponse response = blockingStub.generateId(request);
        System.out.println(response.getSnowFlakeId());
        return response.getSnowFlakeId();
    }
}
