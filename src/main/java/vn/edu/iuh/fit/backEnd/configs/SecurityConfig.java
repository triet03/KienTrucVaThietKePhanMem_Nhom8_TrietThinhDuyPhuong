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
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/companyManager", "/companyManager/**", "/{id}",
                                "/verify", "/verify/company",
                                "/register/**", "/register/send-code",
                                "/login/**", "/logout",  // ✅ Cho phép login/logout cho Candidate
                                "/homeCompany", "/homeCandidate",

                                // ✅ Mở toàn bộ route dành cho Candidate
                                "/dashboardCandidate", "/tranghosonguoidung", "/hoso-cua-ban",
                                "/hoso-candidate", "/save-job/**", "/vieclamdaluu",
                                "/Timkiemvieclam", "/Timkiemvieclam/**",

                                // ✅ Cho frontend load CSS/JS nếu có
                                "/css/**", "/js/**"
                        ).permitAll()

                        // ✅ Chỉ yêu cầu xác thực khi vào trang quản lý công ty
                        .requestMatchers("/company_mangement").authenticated()

                        // Các request còn lại: cũng cho phép
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login/company") // dành cho công ty
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
