package code.adagedo.distributed_id_generator_twitter_snow_flake.service;

public interface ServerIdRegistry {

    void register();
    long createServerId(String serverName);

    void renewLease();
}
