package code.adagedo.distributed_id_generator_twitter_snow_flake.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AuditLogEntry {

    private final long id;
    private final String serverName;
    private final String datacenterName;
    private final long timestamp;
    private final long randomTag;
}
