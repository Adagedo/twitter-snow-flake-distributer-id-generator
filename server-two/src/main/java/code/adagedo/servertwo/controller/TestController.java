package code.adagedo.servertwo.controller;

import code.adagedo.servertwo.service.GrpcClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GrpcClientService clientService;

    @GetMapping("/root")
    public ResponseEntity<String> root(){
        return ResponseEntity.ok("root");
    }

    @GetMapping("/get-id")
    public ResponseEntity<Long> getId() {
        return ResponseEntity.ok(clientService.getSnowFlakeId());
    }
}
