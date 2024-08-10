package uz.cherevichenko.Timesheet.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import uz.cherevichenko.Timesheet.model.Role;
import uz.cherevichenko.Timesheet.model.RoleName;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/timesheets/**").hasAuthority("timesheet") // Изменено на hasAuthority
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/login?error=true")
                )
                .oauth2ResourceServer(oAuth2ResourceServerConfigurer ->
                        oAuth2ResourceServerConfigurer
                                .jwt(jwtConfigurer -> {
                                    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                                    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
                                        List<String> roles = realmAccess != null ? realmAccess.get("roles") : List.of();
                                        return roles.stream()
                                                .map(SimpleGrantedAuthority::new) // Убираем ROLE_ префикс
                                                .collect(Collectors.toList());
                                    });
                                    jwtConfigurer.jwtAuthenticationConverter(converter);
                                })
                )
                .build();
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation("http://localhost:8080/realms/master");
    }


}
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/timesheets/**").hasRole("timesheet")
//                                .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oAuth2ResourceServerConfigurer ->
//                        oAuth2ResourceServerConfigurer
//                                .jwt(jwtConfigurer -> {
//                                    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//                                    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//                                        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
//                                        List<String> roles = realmAccess != null ? realmAccess.get("roles") : List.of();
//                                        return roles.stream()
//                                                .map(SimpleGrantedAuthority::new)
//                                                .collect(Collectors.toList());
//                                    });
//                                    jwtConfigurer.jwtAuthenticationConverter(converter);
//                                })
//                )
//                .build();
//    }
//}
//    @Bean
//    SecurityFilterChain noSecurity(HttpSecurity http) throws Exception {
//        return http
//        .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(it -> it
//                        .requestMatchers("/timesheets/**").hasRole("superUser")
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
//                        .jwt(jwtConfigurer -> {
//                            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//                            converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//                               Map<String,List<String>> realmAccess = jwt.getClaim("realm_access");
//                              List<String> roles = realmAccess.get("roles");
//                              return roles.stream()
//                                      .map(SimpleGrantedAuthority::new)
//                                              .map(it ->(GrantedAuthority) it)
//                                      .toList();
//                            });
//                            jwtConfigurer.jwtAuthenticationConverter(converter);
//
//                        })
//                )
//               // .oauth2ResourceServer(oAuth2ResourceServerConfigurer ->  {
//                 //   oAuth2ResourceServerConfigurer.configure(http);
//                //})
//                .build();
//    }
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/home/projects/**").hasAuthority(RoleName.ADMIN.name())
//                        .requestMatchers("/home/timesheets/**").hasAnyAuthority(RoleName.USER.name())
//                        .requestMatchers("/timesheets/**").hasAuthority(RoleName.REST.name())
//                        .requestMatchers("/projects/**").hasAuthority(RoleName.REST.name())
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults())
//                .build();
//    }


    /*@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Доступ для REST-ресурсов
                        .requestMatchers("/timesheets/**", "/projects/**").authenticated()
                        // Доступ для веб-ресурсов
                        .requestMatchers("/home/projects/**").hasAuthority(RoleName.ADMIN.name())
                        .requestMatchers("/home/timesheets/**").hasAnyAuthority(RoleName.ADMIN.name(), RoleName.USER.name())
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll()
                )
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            // Проверяем, если запрос к REST-ресурсу
            if (request.getRequestURI().startsWith("/timesheets/") || request.getRequestURI().startsWith("/projects/")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            } else {
                // Для остальных запросов, если не авторизован, показываем форму логина
                response.sendRedirect("/login");
            }
        };
    }
}
*/


//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

//}