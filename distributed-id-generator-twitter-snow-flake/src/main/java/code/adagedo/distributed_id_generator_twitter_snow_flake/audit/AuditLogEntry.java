package code.adagedo.distributed_id_generator_twitter_snow_flake.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuditLogEntry {

    private final long id;
    private final String serviceName;
    private final long randomTag;

}
