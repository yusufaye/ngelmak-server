package org.open.ngelmakproject.config;

import static org.open.ngelmakproject.security.SecurityUtils.JWT_ALGORITHM;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.open.ngelmakproject.security.DomainUserDetailsService;
import org.open.ngelmakproject.web.filter.SpaWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Autowired
    private DomainUserDetailsService userDetailsService;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:4200");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll();
                    auth.requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate")).permitAll();
                    auth.requestMatchers(mvc.pattern("/api/register")).permitAll();
                    auth.requestMatchers(mvc.pattern("/api/activate")).permitAll();
                    auth.requestMatchers(mvc.pattern("/api/account/reset-password/init")).permitAll();
                    auth.requestMatchers(mvc.pattern("/api/account/reset-password/finish")).permitAll();
                    // auth.requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN);
                    auth.requestMatchers(mvc.pattern("/api/**")).authenticated();
                    // auth.requestMatchers(mvc.pattern("/v3/api-docs/**")).hasAuthority(AuthoritiesConstants.ADMIN);
                    // auth.requestMatchers(mvc.pattern("/management/health")).permitAll();
                    // auth.requestMatchers(mvc.pattern("/management/health/**")).permitAll();
                    // auth.requestMatchers(mvc.pattern("/management/info")).permitAll();
                    // auth.requestMatchers(mvc.pattern("/management/prometheus")).permitAll();
                    // auth.requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN);
                })
                .exceptionHandling(
                        exceptions -> exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                // .httpBasic(Customizer.withDefaults()) // basic auth (optional, we are not
                // using it)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // oath2 bearer token
                .build();
    }

    /**
     * This specifies the class Spring should use for authentication.
     * 
     * @param http
     * @param bCryptPasswordEncoder
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM)
                .build();
        return token -> {
            return jwtDecoder.decode(token);
        };
    }

    /**
     * This bean is used for generating the jwt.
     * 
     * @return
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * [todo]
     * 
     * @param introspector
     * @return
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

}
