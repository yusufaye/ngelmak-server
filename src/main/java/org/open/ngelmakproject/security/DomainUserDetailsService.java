package org.open.ngelmakproject.security;

import java.util.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.open.ngelmakproject.domain.Authority;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        if (new EmailValidator().isValid(login, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        UserDetails userDetails = userRepository
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
        System.out.println(userDetails);
        return userDetails;
        // return userRepository
        //     .findOneWithAuthoritiesByLogin(lowercaseLogin)
        //     .map(user -> createSpringSecurityUser(lowercaseLogin, user))
        //     .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    /**
     * This method is equivalent of implementing UserDetails.
     * It maps the given User instance to UserDetails instance with associated authorities.
     * @param lowercaseLogin
     * @param user
     * @return
     */
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<SimpleGrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .map(SimpleGrantedAuthority::new)
            .toList();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }
}
