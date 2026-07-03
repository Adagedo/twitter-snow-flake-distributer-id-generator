package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServerIdRegistry implements ServerIdRegistry{

    private final RedisKeyValueService redisKeyValueService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final long MAX_SIZE = 32;

    private static final int LEASE_TTL_MINUTES = 2;

    private String datacenterName;

    private final Map<String, Long> activeServers = new ConcurrentHashMap<>();

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
        }catch (Exception e) {
            log.error("Failed to register Snowflake node. Halting context : {}.", e.getCause().getMessage());
            throw new IllegalStateException("Snowflake node registration failed", e);
        }
    }

    @Override
    public long createServerId(String serverName) {

        if(activeServers.containsKey(serverName)) return activeServers.get(serverName);

        for (long slot = 0; slot < MAX_SIZE; slot++){

            String key = String.format("snowflake:datacenter:%d:worker:%d", datacenterId, slot);
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, serverName, Duration.ofMinutes(LEASE_TTL_MINUTES));

            if(Boolean.TRUE.equals(acquired)) {
                activeServers.put(serverName,  slot);
                log.info("Successfully bound server '{}' to Worker ID slot {}", serverName, slot);
                return slot;
            }
        }
        throw new IllegalStateException("Out of worker ID slots! All 32 positions inside datacenter " + datacenterId + " are occupied.");
    }

    @Override
    @Scheduled(fixedRate = 30000)
    public void renewLease() {

        activeServers.forEach((serverName, serverId) -> {
            String key = buildKey(this.datacenterId, serverId);
            Boolean extend = redisTemplate.expire(key, Duration.ofMinutes(LEASE_TTL_MINUTES));
            if(Boolean.FALSE.equals(extend)){
                log.warn("Lease expired unexpectedly for key: {}. Re-acquiring slot...", key);
                redisTemplate.opsForValue().set(key, serverName, Duration.ofMinutes(LEASE_TTL_MINUTES));
            }

        });
    }

    private String buildKey(long datacenterId, long serverId){
        return "snowflake:dc:" + datacenterId + ":worker:" + serverId;
    }

}
