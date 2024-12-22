package org.open.ngelmakproject.config;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.open.ngelmakproject.domain.Authority;
import org.open.ngelmakproject.domain.Privilege;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.repository.AuthorityRepository;
import org.open.ngelmakproject.repository.ConfigRepository;
import org.open.ngelmakproject.repository.PrivilegeRepository;
import org.open.ngelmakproject.repository.UserRepository;
import org.open.ngelmakproject.security.AuthoritiesConstants;
import org.open.ngelmakproject.security.PrivilegesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StartupConfig {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    
    @PostConstruct
    private void initPrivileges() {
        Set<Privilege> privileges = new HashSet<Privilege>(privilegeRepository.findAll());
        for (Field field : PrivilegesConstants.class.getFields()) {
            try {
                privileges.add(new Privilege().name(field.get(null).toString()));
            } catch (IllegalArgumentException | IllegalAccessException e) {
            }
        }
        privilegeRepository.saveAll(privileges);
    }

    /**
     * Thi method is only for initiating the admin account and nothing else.
     * This is required for at least having the admin user for management of the
     * application.
     * Actions performed by this method is ignore when there is at least one user,
     * either with ADMIN authority or not.
     */
    @PostConstruct
    private void setUpAdminAccount() {
        Set<Authority> authorities = new HashSet<Authority>(authorityRepository.findAll());
        for (Field field : AuthoritiesConstants.class.getFields()) {
            try {
                authorities.add(new Authority().name(field.get(null).toString()));
            } catch (IllegalArgumentException | IllegalAccessException e) {
            }
        }
        authorityRepository.saveAll(authorities);

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            User user;
            user = new User();
            user.setId(1l);
            user.setLogin("admin");
            user.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
            user.setFirstName("Administrator");
            user.setLastName("Administrator");
            user.setEmail("admin@localhost");
            user.setActivated(true);
            user.setLastModifiedDate(Instant.now());
            users.add(user);
            user.setAuthorities(
                    authorities.stream().filter(e -> e.getName().equals(AuthoritiesConstants.ADMIN))
                            .collect(Collectors.toSet()));
            user = new User();
            user.setId(2l);
            user.setLogin("user");
            user.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
            user.setFirstName("User");
            user.setLastName("User");
            user.setEmail("user@localhost");
            user.setActivated(true);
            user.setLastModifiedDate(Instant.now());
            users.add(user);

            user.setAuthorities(
                    authorities.stream().filter(e -> e.getName().equals(AuthoritiesConstants.USER))
                            .collect(Collectors.toSet()));
            userRepository.saveAll(users);
        }
    }

    // @PostConstruct
    // private void setDefaultConfig() {
    // List<Config> configs = configRepository.findAll();
    // if (configs.isEmpty()) {
    // Config config = new Config();
    // config.
    // }
    // }
}
