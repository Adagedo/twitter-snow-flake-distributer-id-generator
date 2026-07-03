package code.adagedo.distributed_id_generator_twitter_snow_flake.engine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Data
@Slf4j
public class IdGeneratorEngine {

    private static final long TWITTER_EPOCH = 1288834974657L;

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final Pattern AGENT_PATTERN = Pattern.compile("^([a-zA-Z][a-zA-Z\\-0-9]*)$");

    private final long workerId;
    private final long datacenterId;
    private final Random rand;

    private long sequence;
    private long lastTimestamp = -1L;


    public IdGeneratorEngine(long workerId, long datacenterId){
        this(workerId, datacenterId,0L);
    }

    public IdGeneratorEngine(long workerId, long datacenterId, long sequence){
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.rand = new Random();
    }


    // logic
}
