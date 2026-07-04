package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServerIdRegistryImplementation implements ServerIdRegistry{

    private final RedisTemplate<String, Long> redisTemplate;
    private static final long MAX_SIZE = 32;
    private static final int LEASE_TTL_MINUTES = 2;

    @Getter
    private String datacenterName;
    @Getter
    private String serverName;

    private final Map<String, Long> ACTIVE_DATACENTER_SERVERS = new ConcurrentHashMap<>();


    @Override
    public long createDatacenterId(String datacenterName) {
        this.datacenterName = datacenterName;
        String resourceType = "Datacenter";
        String catchKey =  resourceType + "datacenter-slot" + datacenterName;
        if(ACTIVE_DATACENTER_SERVERS.containsKey(catchKey)) return ACTIVE_DATACENTER_SERVERS.get(catchKey);
        return createIds(datacenterName, resourceType);
    }

    @Override
    public long createServerId(String serverName) {
        this.serverName = serverName;
        String resourceType = "Server";
        String catchKey =  resourceType + "datacenter-slot" + serverName;
        if(ACTIVE_DATACENTER_SERVERS.containsKey(catchKey)) return ACTIVE_DATACENTER_SERVERS.get(catchKey);
        return createIds(serverName, resourceType);
    }

    private long createIds(String name, String resourceType){
        for (long slot = 0; slot < MAX_SIZE; slot++) {
            String slotKey = resourceType + "datacenter-slot:" + slot;
            String catchKey =  resourceType + "datacenter-slot" + name;

            Long slotValue = System.currentTimeMillis();
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(slotKey, slotValue, Duration.ofMinutes(LEASE_TTL_MINUTES));

            if (Boolean.TRUE.equals(acquired)) {
                ACTIVE_DATACENTER_SERVERS.put(catchKey, slot);
                log.info("Successfully assigned {} {} to slot {}", resourceType, name, slot);
                return slot;
            }
        }

        throw new IllegalStateException("Out of ID slots! All 32 positions are occupied.");
    }
}
