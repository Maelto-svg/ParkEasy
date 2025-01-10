package com.project.parking.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder =passwordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Utilisateur standard
        manager.createUser(User.withUsername("user")
                .password(encoder.encode("password"))
                .roles(ROLE_USER)
                .build());

        // Administrateur
        manager.createUser(User.withUsername("admin")
                .password(encoder.encode("adminpassword"))
                .roles(ROLE_ADMIN)
                .build());
        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/login", "/html/**", "/css/**", "/js/**", "/api/parking/**", "/console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/html/parking.html")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll()).csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(withDefaults());

        return http.build();
    }
}

