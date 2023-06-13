package com.softserve.user.controller;

import com.softserve.commons.constant.RoleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
@Slf4j
public class TokenController {
    @GetMapping("/parse")
    public ResponseEntity<Void> parseJwtForNginx(@RequestHeader("Authorization") String token) {
        log.info("Parsing token request");
        //todo
        System.out.println(token);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("user_id", "1");
        responseHeaders.set("username", "testUsername");
        responseHeaders.set("role", String.valueOf(RoleData.ADMIN));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .build();
    }
}
