package code.adagedo.distributed_id_generator_twitter_snow_flake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);

        // String serializers
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Long serializers
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
