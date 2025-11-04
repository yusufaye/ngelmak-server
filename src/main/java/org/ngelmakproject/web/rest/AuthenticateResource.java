package org.ngelmakproject.web.rest;

import java.util.Optional;

import org.ngelmakproject.service.AuthenticateService;
import org.ngelmakproject.web.rest.dto.LoginRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class AuthenticateResource {

private static final Logger log = LoggerFactory.getLogger(AuthenticateResource.class);
    @Autowired
    private AuthenticateService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new JWTToken(token));
    }

    // @GetMapping("/validate")
    // public ResponseEntity<Void> validateToken(
    //         @RequestHeader("Authorization") String authHeader) {

    //     // Authorization: Bearer <token>
    //     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }

    //     return authService.validateToken(authHeader.substring(7))
    //             ? ResponseEntity.ok().build()
    //             : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    // }

    
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
