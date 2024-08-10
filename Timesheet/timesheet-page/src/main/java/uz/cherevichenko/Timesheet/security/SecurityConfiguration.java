package uz.cherevichenko.Timesheet.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.function.Consumer;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/timesheets/**").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(customAuthenticationSuccessHandler())
                                .failureUrl("/login?error")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                // Перенаправление на ресурс на другом сервере
                response.sendRedirect("http://localhost:8080/timesheets");
            }
        };
    }
}

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/auth/**").permitAll() // Разрешить доступ к URL "/auth/**" без аутентификации
//                                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/auth/login") // Указать путь к странице авторизации
//                );
//                // Отключить CSRF для упрощения, но не рекомендуется для production
//
//        return http.build();
//    }
//}


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/timesheets/**").authenticated()
//                                .anyRequest().permitAll()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .clientRegistrationRepository(clientRegistrationRepository())
//                );
//        return http.build();
//    }
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration keycloakClientRegistration = ClientRegistration.withRegistrationId("keycloak")
//                .clientId("unsafe_client")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .authorizationUri("http://localhost:8080/realms/master/protocol/openid-connect/auth")
//                .tokenUri("http://localhost:8080/realms/master/protocol/openid-connect/token")
//                .userInfoUri("http://localhost:8080/realms/master/protocol/openid-connect/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("http://localhost:8080/realms/master/protocol/openid-connect/certs")
//                .clientName("Keycloak")
//                .build();
//
//        return new InMemoryClientRegistrationRepository(keycloakClientRegistration);
//    }
//}
//    @Autowired
//    private ClientRegistrationRepository clientRegistrationRepository;

//    @Bean
//    ClientRegistrationRepository clientRegistrationRepository(){
//        return new InMemoryClientRegistrationRepository(
//                ClientRegistration.withRegistrationId("keycloak")
//                        .clientId("unsafe_client")
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .redirectUri("http://localhost:3333/oauth2/authorization")
//                        .authorizationUri("http://localhost:8080/realms/master/protocol/openid-connect/auth")
//                        .tokenUri("/tokenasafdf")
//                        .build()
//        );
//
//    }
//
//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults(){
//        return new GrantedAuthorityDefaults("");
//    }
//    @Bean
//    SecurityFilterChain noSecurity(HttpSecurity http,ClientRegistrationRepository clientRegistrationRepository) throws Exception {
//
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(it -> it.anyRequest().authenticated())
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorization -> authorization
//                        .authorizationRequestResolver(
//                                authorizationRequestResolver(clientRegistrationRepository)
//                        )
//                        )
//
//                )
//
//                .build();
//
//    }
//
//    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
//            ClientRegistrationRepository clientRegistrationRepository){
//
//        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
//                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
//                        "/oauth2/authorization");
//        authorizationRequestResolver.setAuthorizationRequestCustomizer(
//                authorizationRequestCustomizer());
//        return authorizationRequestResolver;
//    }
//    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer(){
//        return  customizer -> customizer
//                .additionalParameters(params -> params.put("prompt","consent"));
//    }
////    @Bean
////    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        return http
////                .authorizeHttpRequests(request -> request
////                        .requestMatchers("/home/projects/**").hasAuthority(RoleName.ADMIN.name())
////                        .requestMatchers("/home/timesheets/**").hasAnyAuthority(RoleName.USER.name())
////                        .requestMatchers("/timesheets/**").hasAuthority(RoleName.REST.name())
////                        .requestMatchers("/projects/**").hasAuthority(RoleName.REST.name())
////                        .requestMatchers("/h2-console/**").permitAll()
////                        .anyRequest().authenticated()
////                )
////                .formLogin(Customizer.withDefaults())
////                .build();
////    }
//
//
//    /*@Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        // Доступ для REST-ресурсов
//                        .requestMatchers("/timesheets/**", "/projects/**").authenticated()
//                        // Доступ для веб-ресурсов
//                        .requestMatchers("/home/projects/**").hasAuthority(RoleName.ADMIN.name())
//                        .requestMatchers("/home/timesheets/**").hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(customAuthenticationEntryPoint())
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
//        return (request, response, authException) -> {
//            // Проверяем, если запрос к REST-ресурсу
//            if (request.getRequestURI().startsWith("/timesheets/") || request.getRequestURI().startsWith("/projects/")) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            } else {
//                // Для остальных запросов, если не авторизован, показываем форму логина
//                response.sendRedirect("/login");
//            }
//        };
//    }
//}
//*/
//
//
//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
