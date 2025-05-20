package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.services.CompanyService;
import vn.edu.iuh.fit.backEnd.services.VerificationService;

@Controller
@RequestMapping("/register/company")
public class CompanyRegisterController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private VerificationService verificationService;

    @GetMapping
    public String showRegisterPage() {
        return "company_register";
    }

    @PostMapping("/send-code")
    @ResponseBody
    public String sendCode(@RequestParam String email) {
        if (!StringUtils.hasText(email) || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            return "invalid";

        if (companyService.existsByEmail(email)) return "exists";

        verificationService.generateAndSendCode(email);
        return "sent";
    }

    @PostMapping("/confirm")
    public String confirmRegister(@RequestParam String compName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String phone,
                                  @RequestParam String webUrl,
                                  @RequestParam String about,
                                  @RequestParam String verifyCode,
                                  RedirectAttributes redirectAttributes) {

        String expectedCode = verificationService.getVerificationCode(email);
        if (expectedCode == null || !expectedCode.equals(verifyCode)) {
            redirectAttributes.addFlashAttribute("error", "❌ Mã xác thực không đúng hoặc đã hết hạn.");
            return "redirect:/register/company";
        }

        if (companyService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "❌ Email đã tồn tại.");
            return "redirect:/register/company";
        }

        Company company = new Company();
        company.setCompName(compName);
        company.setEmail(email);
        company.setPassword(password);
        company.setPhone(phone);
        company.setWebUrl(webUrl);
        company.setAbout(about);
        company.setEmailVerified(true);

        companyService.save(company);
        verificationService.removeCode(email);

        // ✅ Chuyển hướng sang trang đăng nhập và giữ thông báo thành công
        redirectAttributes.addFlashAttribute("success", "✅ Đăng ký thành công! Mời đăng nhập.");
        return "redirect:/login/company";
    }
}


