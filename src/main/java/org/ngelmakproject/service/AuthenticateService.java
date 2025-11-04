package org.ngelmakproject.service;

import java.util.Optional;

import org.ngelmakproject.repository.UserRepository;
import org.ngelmakproject.security.JwtUtil;
import org.ngelmakproject.web.rest.dto.LoginRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users.
 */
@Service
public class AuthenticateService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateService.class);

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userRepository.findOneByLogin(loginRequestDTO.getUsername())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getLogin(), u.getAuthorities()));

        return token;
    }

}
