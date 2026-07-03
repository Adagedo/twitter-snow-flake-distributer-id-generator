package code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions;

public class InvalidUserServiceError extends RuntimeException{

    public InvalidUserServiceError(){
        super("Invalid service name exceptions");
    }
}
