package code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions;

public class InvalidDatacenterException extends RuntimeException{

    public InvalidDatacenterException(String message){
        super(message);
    }
}
