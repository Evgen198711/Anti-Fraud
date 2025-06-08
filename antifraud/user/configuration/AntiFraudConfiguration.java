package antifraud.user.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AntiFraudConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)

                .exceptionHandling(handing -> handing
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()))) // Handles auth error

                .headers(s -> s.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(requests -> requests

                                .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/auth/user/*").hasRole("ADMINISTRATOR")
                                .requestMatchers("/api/auth/list").hasAnyRole("SUPPORT", "ADMINISTRATOR")
                                .requestMatchers("/api/auth/access", "/api/auth/role").hasRole("ADMINISTRATOR")
                                .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasRole("MERCHANT")
                                .requestMatchers("/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/*").hasRole("SUPPORT")
                                .requestMatchers("/api/antifraud/stolencard", "/api/antifraud/stolencard/*").hasRole("SUPPORT")
                                .requestMatchers(HttpMethod.GET,"/api/antifraud/history", "/api/antifraud/history/*").hasRole("SUPPORT")
                                .requestMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasRole("SUPPORT")

                                .requestMatchers("/h2-console/**", "/error", "/actuator/shutdown").permitAll()



                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
