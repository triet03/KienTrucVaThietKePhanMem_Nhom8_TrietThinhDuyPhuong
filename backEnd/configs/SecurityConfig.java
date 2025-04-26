package vn.edu.iuh.fit.backEnd.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.iuh.fit.backEnd.services.CompanyDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CompanyDetailsService companyDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CompanyDetailsService companyDetailsService, PasswordEncoder passwordEncoder) {
        this.companyDetailsService = companyDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/register/company", "/register/**",
                                "/verify/company", "/verify", "/login/**",
                                "/homeCompany", "/homeCandidate",
                                "/jobs", "/jobs/**",
                                "/candidates/{id}", "/{id}",
                                "/css/**", "/js/**", "/images/**",
                                "/companyManager/**"
                        ).permitAll()

                        // ✅ Bảo vệ toàn bộ route liên quan đến companyManager
                        .requestMatchers("/companyManager/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login/company")
                        .defaultSuccessUrl("/companyManager", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/homeCompany")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return companyDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(companyDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
