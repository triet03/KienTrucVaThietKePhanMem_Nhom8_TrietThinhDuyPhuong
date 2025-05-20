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
                .csrf().disable() // ⚠️ Tắt CSRF nếu dùng fetch/ajax, bật lại nếu dùng form submit chuẩn
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/companyManager", "/companyManager/**", "/{id}",
                                "/verify", "/verify/company",
                                "/register/**", "/register/send-code",     // ✅ Cho phép gửi mã không cần đăng nhập
                                "/login/**",
                                "/homeCompany", "/homeCandidate",
                                "/jobs", "/jobs/**",
                                "/css/**", "/js/**"
                        ).permitAll()
                        .requestMatchers("/company_mangement").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
//                        .loginPage("/login/company")
                        .defaultSuccessUrl("/companyManage", true)
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
