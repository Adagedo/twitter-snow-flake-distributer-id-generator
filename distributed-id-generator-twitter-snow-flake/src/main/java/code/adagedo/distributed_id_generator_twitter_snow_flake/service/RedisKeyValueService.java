//package code.adagedo.distributed_id_generator_twitter_snow_flake.service;
//
//import code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions.InvalidDatacenterException;
//import code.adagedo.distributed_id_generator_twitter_snow_flake.models.Datacenter;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class RedisKeyValueService {
//
//    private final RedisTemplate<String, Long> redisTemplate;
//
//    @PostConstruct
//    public void save(){
//        String KEY = "snowflake:datacenter";
//        if (!redisTemplate.hasKey(KEY)){
//            HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
//            for (Datacenter datacenter: Datacenter.values()){
//                hash.put(KEY, datacenter.getName(), datacenter.getId());
//            }
//        }
//    }
//
//    public long getDataCenterId(String key, String dataCenterName){
//        HashOperations<String, String, Long> hash = redisTemplate.opsForHash();
//        if(!hash.hasKey(key, dataCenterName)){
//            throw new InvalidDatacenterException("Data center not registered");
//        }
//        Object datacenterId =  hash.get(key, dataCenterName);
//        if (datacenterId == null){
//            log.info("datacenter id cannot be null");
//            throw new IllegalArgumentException("unknown datacenter name " + dataCenterName);
//        }
//        return Long.parseLong(datacenterId.toString());
//    }
//}
