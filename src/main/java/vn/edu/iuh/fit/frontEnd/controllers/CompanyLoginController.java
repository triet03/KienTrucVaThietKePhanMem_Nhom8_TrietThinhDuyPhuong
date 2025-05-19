package vn.edu.iuh.fit.frontEnd.controllers;

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
    private CompanyRepository companyRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ 1. Hiển thị form đăng ký nhà tuyển dụng
    @GetMapping("/register/company")
    public String showCompanyRegisterForm(Model model) {
        model.addAttribute("company", new Company()); // Truyền object `Company` vào model
        return "company_register";
    }

    // ✅ 2. Xử lý đăng ký nhà tuyển dụng
    @PostMapping("/register/company")
    public String processCompanyRegister(@ModelAttribute("company") Company company, Model model) {
        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            model.addAttribute("error", "❌ Email đã tồn tại!");
            return "company_register";
        }

        // Mã hóa mật khẩu
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        company.setVerificationToken(UUID.randomUUID().toString());
        company.setEmailVerified(false);

        companyRepository.save(company);

        // Gửi email xác thực
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
                + "<p><a href='" + verifyUrl + "' style='color:blue;'>Xác thực tài khoản</a></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(company.getEmail());
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    // ✅ 4. Xác thực email & tự động đăng nhập
    @GetMapping("/verify/company")
    public String verifyEmail(@RequestParam String token, Model model) {
        Optional<Company> companyOptional = companyRepository.findByVerificationToken(token);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setEmailVerified(true);
            company.setVerificationToken(null); // Xóa token sau khi xác thực
            companyRepository.save(company);

            // Tự động đăng nhập sau khi xác thực
            Authentication authentication = new UsernamePasswordAuthenticationToken(company, null, company.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/companyManage"; // Chuyển hướng đến dashboard
        } else {
            model.addAttribute("message", "Mã xác thực không hợp lệ hoặc đã hết hạn!");
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
        System.out.println("🔹 Đang xử lý đăng nhập với email: " + email);

        Optional<Company> companyOptional = companyRepository.findByEmail(email);

        if (companyOptional.isEmpty()) {
            System.out.println("❌ Email không tồn tại trong database!");
            model.addAttribute("error", "❌ Email không tồn tại!");
            return "company_login";
        }

        Company company = companyOptional.get();
        System.out.println("✅ Tìm thấy tài khoản: " + company.getEmail());

        if (!company.isEmailVerified()) {
            System.out.println("❌ Email chưa được xác thực!");
            model.addAttribute("error", "❌ Email chưa xác thực. Vui lòng kiểm tra email.");
            return "company_login";
        }

        System.out.println("🔹 Đang kiểm tra mật khẩu...");
        if (!passwordEncoder.matches(password, company.getPassword())) {
            System.out.println("❌ Mật khẩu không chính xác!");
            model.addAttribute("error", "❌ Mật khẩu không chính xác!");
            return "company_login";
        }

        System.out.println("✅ Đăng nhập thành công!");

        // Tạo phiên đăng nhập
        Authentication authentication = new UsernamePasswordAuthenticationToken(company, null, company.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/companyManage"; // Chuyển hướng đến dashboard
    }


    // ✅ 7. Hiển thị trang quản lý nhà tuyển dụng
    @GetMapping("/companyManage")
    public String showCompanyManagement(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Company) {
            Company company = (Company) authentication.getPrincipal();
            model.addAttribute("company", company);
        }
        return "QuanLyNhaTuyenDung";
    }
}
