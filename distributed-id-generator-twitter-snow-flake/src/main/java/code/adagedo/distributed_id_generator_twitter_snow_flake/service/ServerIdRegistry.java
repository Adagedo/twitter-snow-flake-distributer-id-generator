package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

public interface ServerIdRegistry {
    long createDatacenterId(String datacenterName);
    long createServerId(String serverName);
}
