package code.adagedo.distributed_id_generator_twitter_snow_flake.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Datacenter {

    EU_CENTRAL_1("eu-central-1", 12L),
    US_WEST_2("us-west-2", 23L),
    US_EAST_1("us-east-1", 15L),

    EUROPE_WEST1("europe-west1", 19L),
    EUROPE_WEST2("europe-west2", 9L),
    ASIA_SOUTHEAST1("asia-southeast1", 3L),

    NORTH_EUROPE("northeurope", 17L),
    SOUTHEAST_ASIA("southeastasia", 27L),
    JAPAN_EAST("japaneast", 11L);

    private final String name;
    private final long id;
}