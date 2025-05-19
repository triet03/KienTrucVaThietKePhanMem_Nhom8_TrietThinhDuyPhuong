package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.services.CandidateService;
import vn.edu.iuh.fit.backEnd.services.VerificationService;

@Controller
public class CandidateRegisterController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/register/candidate")
    public String showRegisterForm() {
        return "candidate_register";
    }

    // ✅ Sửa lại endpoint để tránh trùng
    @PostMapping("/register/confirm")
    public String handleRegister(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String verifyCode,
            Model model,
            HttpSession session) {

        String expectedCode = verificationService.getVerificationCode(email);
        if (expectedCode == null || !expectedCode.equals(verifyCode)) {
            model.addAttribute("error", "Mã xác thực không đúng hoặc đã hết hạn.");
            return "candidate_register";
        }
    

        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setEmail(email);
        candidate.setPassword(password); // Không cần mã hóa theo yêu cầu
        candidateService.saveCandidate(candidate);
        verificationService.removeCode(email);

        return "redirect:/login/candidate";
    }

    @PostMapping("/register/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email) {
        try {
            if (!StringUtils.hasText(email) || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return "invalid";
            }
            if (candidateService.existsByEmail(email)) {
                return "exists";
            }

            String code = verificationService.generateAndSendCode(email);
            System.out.println("✅ Đã tạo mã: " + code + " cho " + email);
            return "sent";

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi mã xác thực:");
            e.printStackTrace();
            return "error";  // để JS biết là lỗi thật
        }
    }

}
