package uz.cherevichenko.Timesheet.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain noSecurity(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(it -> it.anyRequest().permitAll()).build();
    }
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
}