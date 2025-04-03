package vn.edu.iuh.fit.backEnd.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

import java.util.Optional;

@Service
public class CompanyDetailsService implements UserDetailsService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder; // Nhận từ AppConfig

    public CompanyDetailsService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Company company = companyRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));

        if (!company.isEmailVerified()) {
            throw new UsernameNotFoundException("Tài khoản chưa xác thực email!");
        }

        return User.builder()
                .username(company.getEmail())
                .password(company.getPassword()) // Spring Security sẽ tự động kiểm tra mật khẩu
                .roles("USER")
                .build();
    }
}
