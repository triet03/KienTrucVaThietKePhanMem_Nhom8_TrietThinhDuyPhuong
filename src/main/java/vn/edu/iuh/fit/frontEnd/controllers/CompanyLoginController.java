package vn.edu.iuh.fit.frontEnd.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

@Controller
public class CompanyLoginController {
    @Autowired
    private RateLimiterRegistry rateLimiterRegistry; // ✅ Thêm dòng này
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ 1. Hiển thị form đăng ký nhà tuyển dụng
    @GetMapping("/register/company")
    public String showCompanyRegisterForm(Model model) {
        model.addAttribute("company", new Company());
        return "company_register";
    }

    // ✅ 2. Xử lý đăng ký
    @PostMapping("/register/company")
    public String processCompanyRegister(@ModelAttribute("company") Company company, Model model) {
        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            model.addAttribute("error", "❌ Email đã tồn tại!");
            return "company_register";
        }

        company.setPassword(passwordEncoder.encode(company.getPassword()));
        company.setVerificationToken(UUID.randomUUID().toString());
        company.setEmailVerified(false);
        companyRepository.save(company);

        try {
            sendVerificationEmail(company);
        } catch (MessagingException e) {
            model.addAttribute("error", "❌ Lỗi khi gửi email xác thực!");
            return "company_register";
        }

        model.addAttribute("message", "✅ Đăng ký thành công! Vui lòng kiểm tra email để xác thực.");
        return "company_login";
    }

    // ✅ 3. Gửi email xác thực
    private void sendVerificationEmail(Company company) throws MessagingException {
        String subject = "🔒 Xác thực tài khoản Nhà tuyển dụng";
        String verifyUrl = "http://localhost:9998/verify/company?token=" + company.getVerificationToken();
        String body = "<p>Chào <b>" + company.getCompName() + "</b>,</p>"
                + "<p>Vui lòng nhấn vào đường link dưới đây để xác thực tài khoản của bạn:</p>"
                + "<p><a href='" + verifyUrl + "'>Xác thực tài khoản</a></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(company.getEmail());
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    // ✅ 4. Xác thực email & đăng nhập
    @GetMapping("/verify/company")
    public String verifyEmail(@RequestParam String token, Model model) {
        Optional<Company> companyOptional = companyRepository.findByVerificationToken(token);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setEmailVerified(true);
            company.setVerificationToken(null);
            companyRepository.save(company);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    company, null, company.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/companyManager"; // ✅ CHUẨN: giống CompanyManagerController
        } else {
            model.addAttribute("message", "❌ Mã xác thực không hợp lệ hoặc đã hết hạn!");
            model.addAttribute("success", false);
            return "verification_result";
        }
    }

    // ✅ 5. Hiển thị form đăng nhập
    @GetMapping("/login/company")
    public String showCompanyLoginForm(@RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "logout", required = false) String logout,
                                       Model model) {
        if (error != null) {
            model.addAttribute("error", "❌ Sai email hoặc mật khẩu!");
        }
        if (logout != null) {
            model.addAttribute("message", "✅ Đã đăng xuất thành công!");
        }
        return "company_login";
    }

    // ✅ 6. Xử lý đăng nhập
    @PostMapping("/login/company")
    public String processCompanyLogin(@RequestParam String email,
                                      @RequestParam String password,
                                      Model model) {

        RateLimiter limiter = rateLimiterRegistry.rateLimiter("loginCompanyLimiter");

        try {
            // Logic được xử lý bên trong lambda, nhưng kết quả được gán ra ngoài
            String[] resultHolder = new String[1];
            RateLimiter.decorateRunnable(limiter, () -> {
                Optional<Company> companyOptional = companyRepository.findByEmail(email);

                if (companyOptional.isEmpty()) {
                    resultHolder[0] = showError(model, "❌ Tài khoản không tồn tại!");
                    return;
                }

                Company company = companyOptional.get();

                if (!passwordEncoder.matches(password, company.getPassword())) {
                    resultHolder[0] = showError(model, "❌ Sai mật khẩu!");
                    return;
                }

                if (!company.isEmailVerified()) {
                    resultHolder[0] = showError(model, "❌ Tài khoản chưa được xác thực email!");
                    return;
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        company, null, company.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                resultHolder[0] = "redirect:/companyManager";
            }).run();

            return resultHolder[0];

        } catch (RequestNotPermitted e) {
            model.addAttribute("error", "⚠️ Đã vượt quá số lần đăng nhập cho phép. Vui lòng thử lại sau 1 phút.");
            return "company_login";
        }
    }

    private String showError(Model model, String errorMsg) {
        model.addAttribute("error", errorMsg);
        return "company_login";
    }
}