package code.adagedo.server.config;

import code.adagedo.commonproto.ComputeIdGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel managedChannel(){
        return
                ManagedChannelBuilder
                .forAddress("localhost", 8090)
                .usePlaintext()
                .build();
    }

    @Bean
    public ComputeIdGrpc.ComputeIdBlockingStub blockingStub(){
        return ComputeIdGrpc.newBlockingStub(managedChannel());
    }
}
