package code.adagedo.server.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}
