package code.adagedo.distributed_id_generator_twitter_snow_flake.config;

import code.adagedo.distributed_id_generator_twitter_snow_flake.engine.IdGeneratorEngine;
import code.adagedo.distributed_id_generator_twitter_snow_flake.service.RedisServerIdRegistryImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfiguration {

    @Bean
    public IdGeneratorEngine idGeneratorEngine(RedisServerIdRegistryImplementation redisServerIdRegistry){

        return new IdGeneratorEngine(
                redisServerIdRegistry.createServerId(redisServerIdRegistry.getServerName()),
                redisServerIdRegistry.createDatacenterId(redisServerIdRegistry.getDatacenterName()),
                redisServerIdRegistry.getServerName(),
                redisServerIdRegistry.getDatacenterName()
        );
    }
}
