package com.example.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http
                .formLogin()
                    .loginPage("/custom-login")
                .and()
                .build();
    }

    /**
     * Configuration of the OAuth2 client.
     * Multiple clients can be configured. The current implementation stores them in memory, but a table
     * in the database can be used to store all the registered clients.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // client-id and client-secret that must be used from all the OAuth2 clients
                .clientId("student-client")
                .clientSecret("$2a$12$3WH/5wbN7L5fPznjqfkkPu50HKydK8y12PuaxDpcBQwukFPXtjF1m")
                // the Basic authentication method will be used between backend-client and backend-auth
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // grant types to be used
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // permitted redirect URI after the authentication is successful
                .redirectUri("http://client-server:8080/login/oauth2/code/student-client-oidc")
                .redirectUri("http://client-server:8080/authorized")
                // acceptable scopes for the authorization
                .scope(OidcScopes.OPENID)
                .scope("messages")
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * Acceptable URL of the authorization server
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://auth-server:9000")
                .build();
    }
}