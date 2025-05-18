package vn.edu.iuh.fit.backEnd.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

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
        int maxRetries = 3;
        double delay = 3000; // 3s
        double backoff = 1.5;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Company company = companyRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));

                if (!company.isEmailVerified()) {
                    throw new UsernameNotFoundException("Tài khoản chưa xác thực email!");
                }

                if (company.getPassword() == null || company.getPassword().isEmpty()) {
                    throw new IllegalStateException("Mật khẩu không hợp lệ");
                }

                return User.builder()
                        .username(company.getEmail())
                        .password(company.getPassword()) // Spring Security sẽ tự động kiểm tra mật khẩu
                        .roles("USER")
                        .build();

            } catch (Exception e) {
                System.err.println("Lỗi khi xác thực đăng nhập (lần " + attempt + "): " + e.getMessage());

                if (attempt == maxRetries) {
                    throw new UsernameNotFoundException("Không thể xác thực tài khoản, vui lòng thử lại sau.");
                }

                try {
                    Thread.sleep((long) delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry bị ngắt", ie);
                }

                delay *= backoff;
            }
        }

        return null; // Không bao giờ tới đây nhưng cần cho compile
    }
}
