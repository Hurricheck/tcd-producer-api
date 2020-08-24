package com.deliveryhero.demo.producerapi.controller;

import com.deliveryhero.demo.producerapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/api/v1/auth")
    public Mono<ResponseEntity> auth(
            @RequestParam String user,
            @RequestParam String passwordHash
    ) {
        return Mono.just(loginService.handleLogin(user, passwordHash) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build());
    }
}
