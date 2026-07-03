package code.adagedo.distributed_id_generator_twitter_snow_flake.exceptions;

public class InvalidSystemClockException extends RuntimeException{

    public InvalidSystemClockException(String messaged) {
        super(messaged);
    }
}
