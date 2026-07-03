package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import code.adagedo.distributed_id_generator_twitter_snow_flake.models.Datacenter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisKeyValueService {

    private final RedisTemplate<String, Long> redisTemplate;

    public void save(){
        String KEY = "snowflake:datacenter";
        if (redisTemplate.hasKey(KEY)){
            HashOperations<String, String, Long> hash = redisTemplate.opsForHash();

            hash.put(KEY, Datacenter.EU_CENTRAL_1.getName(), Datacenter.EU_CENTRAL_1.getId());
            hash.put(KEY, Datacenter.US_WEST_2.getName(), Datacenter.US_WEST_2.getId());
            hash.put(KEY, Datacenter.US_EAST_1.getName(), Datacenter.US_EAST_1.getId());

            hash.put(KEY, Datacenter.EUROPE_WEST1.getName(), Datacenter.EUROPE_WEST1.getId());
            hash.put(KEY, Datacenter.EUROPE_WEST2.getName(), Datacenter.EUROPE_WEST2.getId());
            hash.put(KEY, Datacenter.ASIA_SOUTHEAST1.getName(), Datacenter.ASIA_SOUTHEAST1.getId());

            hash.put(KEY, Datacenter.NORTH_EUROPE.getName(), Datacenter.NORTH_EUROPE.getId());
            hash.put(KEY, Datacenter.SOUTHEAST_ASIA.getName(), Datacenter.SOUTHEAST_ASIA.getId());
            hash.put(KEY, Datacenter.JAPAN_EAST.getName(), Datacenter.JAPAN_EAST.getId());
        }
    }

    private long getDataCenterId(String key, String dataCenterName){
        HashOperations<String, String, Long> hash = redisTemplate.opsForHash();
        return hash.get(key, dataCenterName);
    }
}
