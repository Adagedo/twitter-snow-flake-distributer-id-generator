package code.adagedo.server.controller;

import code.adagedo.server.service.GrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GrpcClientService clientService;

    @GetMapping("/root")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Root");
    }
    @GetMapping("/get-id")
    public ResponseEntity<Long> getId(){
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getSnowFlakeId());
    }
}
