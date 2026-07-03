package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServerIdRegistry implements ServerIdRegistry{

    private final RedisKeyValueService redisKeyValueService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final long MAX_SIZE = 32;

    private static final int LEASE_TTL_MINUTES = 2;

    private String serverName;

    private String datacenterName;

    @Getter
    private long datacenterId = -1;
    @Getter
    private long serverId = -1;


    @Override
    @PostConstruct
    public void register() {
        String KEY = "snowflake:datacenter";

        try{
             this.datacenterId = redisKeyValueService.getDataCenterId(KEY, datacenterName);
             this.serverId = claimServerSlot(datacenterId, serverName);

        }catch (Exception e) {
            log.error("Failed to register Snowflake node. Halting context : {}.", e.getCause().getMessage());
            throw new IllegalStateException("Snowflake node registration failed", e);
        }
    }

    @Override
    public long claimServerSlot(long datacenterId, String serverName) {

        for (long slot = 0; slot < MAX_SIZE; slot++){

            String key = String.format("snowflake:dc:%d:worker:%d", datacenterId, slot);
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, serverName, Duration.ofMinutes(LEASE_TTL_MINUTES));

            if(Boolean.TRUE.equals(acquired)) return slot;
        }
        throw new IllegalStateException("Out of worker ID slots! All 32 positions inside datacenter " + datacenterId + " are occupied.");
    }

    @Override
    @Scheduled(fixedRate = 30000)
    public void renewLease() {

        if (this.serverId != -1){
            String key = buildKey(this.datacenterId, this.serverId);
            Boolean extend = redisTemplate.expire(key, Duration.ofMinutes(LEASE_TTL_MINUTES));
            if(Boolean.FALSE.equals(extend)){
                log.warn("Lease expired unexpectedly for key: {}. Re-acquiring slot...", key);
                redisTemplate.opsForValue().set(key, serverName, Duration.ofMinutes(LEASE_TTL_MINUTES));
            }
        }
    }

    private String buildKey(long datacenterId, long serverId){
        return "snowflake:dc:" + datacenterId + ":worker:" + serverId;
    }

}
