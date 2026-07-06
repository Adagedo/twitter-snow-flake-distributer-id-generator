package code.adagedo.distributed_id_generator_twitter_snow_flake.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class AuditLogEntry {

    private  long snowFlakeId;
    private  String serverName;
    private  String datacenterName;
    private long serverId;
    private long datacenterId;
    private LocalDateTime timestamp;
}
