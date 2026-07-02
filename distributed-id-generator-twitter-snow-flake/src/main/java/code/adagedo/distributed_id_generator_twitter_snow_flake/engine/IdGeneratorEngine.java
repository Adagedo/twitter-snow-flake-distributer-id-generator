package code.adagedo.distributed_id_generator_twitter_snow_flake.engine;

public class IdGeneratorEngine {

    private String data_center_id;
    private String server_id;

    public IdGeneratorEngine(String data_center_id, String server_id){
        this.data_center_id = data_center_id;
        this.server_id = server_id;
    }

    public synchronized long generate_id(){

        return 0;
    }

    // logic
}
