package com.daniil.apigateway.controller;

import com.daniil.apigateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public Mono<Map<String, String>> getToken(@RequestParam String username) {
        String token = jwtUtil.generateToken(username);
        return Mono.just(Map.of(
                "token", token,
                "username", username
        ));
    }
}
