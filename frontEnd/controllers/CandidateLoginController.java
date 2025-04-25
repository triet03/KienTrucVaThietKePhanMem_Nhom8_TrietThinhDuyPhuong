package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.services.CandidateService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class CandidateLoginController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateLoginController.class);

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("error", null);
        return "candidate_register";
    }

    @PostMapping("/register/candidate")
    public String processCandidateRegistration(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        try {
            Candidate existingCandidate = candidateService.findByEmail(email);
            if (existingCandidate != null) {
                model.addAttribute("error", "Email đã được sử dụng!");
                return "candidate_register";
            }

            session.setAttribute("pendingCandidate", new Candidate());
            session.setAttribute("pendingFullName", fullName);
            session.setAttribute("pendingEmail", email);
            session.setAttribute("pendingPassword", passwordEncoder.encode(password));

            candidateService.sendAndSaveVerificationCode(email);

            return "redirect:/verify-code";
        } catch (Exception e) {
            logger.error("Error during registration for email: {}", email, e);
            model.addAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại!");
            return "candidate_register";
        }
    }

    @GetMapping("/verify-code")
    public String showVerifyCodeForm(Model model) {
        model.addAttribute("error", null);
        return "verify_code";
    }

    @PostMapping("/verify-code")
    public String processVerifyCode(
            @RequestParam String code,
            Model model,
            HttpSession session) {
        String email = (String) session.getAttribute("pendingEmail");
        if (email == null) {
            model.addAttribute("error", "Phiên đăng ký đã hết hạn, vui lòng đăng ký lại!");
            return "verify_code";
        }

        try {
            boolean isValidCode = candidateService.verifyCode(email, code);
            if (isValidCode) {
                Candidate candidate = (Candidate) session.getAttribute("pendingCandidate");
                candidate.setFullName((String) session.getAttribute("pendingFullName"));
                candidate.setEmail(email);
                candidate.setPassword((String) session.getAttribute("pendingPassword"));
                candidateService.createCandidate(candidate);

                session.removeAttribute("pendingCandidate");
                session.removeAttribute("pendingFullName");
                session.removeAttribute("pendingEmail");
                session.removeAttribute("pendingPassword");

                return "redirect:/login/candidate";
            } else {
                model.addAttribute("error", "Mã xác thực không đúng hoặc đã hết hạn!");
                return "verify_code";
            }
        } catch (Exception e) {
            logger.error("Error during code verification for email: {}", email, e);
            model.addAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại!");
            return "verify_code";
        }
    }

    @GetMapping("/resend-code")
    public String resendVerificationCode(Model model, HttpSession session) {
        String email = (String) session.getAttribute("pendingEmail");
        if (email == null) {
            model.addAttribute("error", "Phiên đăng ký đã hết hạn, vui lòng đăng ký lại!");
            return "candidate_register";
        }

        try {
            candidateService.deleteVerificationCodeByEmail(email);
            candidateService.sendAndSaveVerificationCode(email);
            model.addAttribute("message", "Mã xác thực mới đã được gửi đến email của bạn!");
            return "verify_code";
        } catch (Exception e) {
            logger.error("Error during resend code for email: {}", email, e);
            model.addAttribute("error", "Đã có lỗi xảy ra khi gửi lại mã, vui lòng thử lại!");
            return "verify_code";
        }
    }

    @GetMapping("/login/candidate")
    public String showLoginForm(Model model) {
        model.addAttribute("error", null);
        return "candidate_login";
    }

    @PostMapping("/login/candidate")
    public String processCandidateLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        try {
            Candidate candidate = candidateService.findByEmail(email);
            if (candidate != null && passwordEncoder.matches(password, candidate.getPassword())) {
                session.setAttribute("loggedInCandidate", candidate);

                // KHÔNG sử dụng AuthenticationManager ở đây
                // Vì chúng ta không cấu hình UserDetailsService riêng

                return "redirect:/candidates/dashboard";
            } else {
                model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
                return "candidate_login";
            }
        } catch (Exception e) {
            logger.error("Error during login for email: {}", email, e);
            model.addAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại!");
            return "candidate_login";
        }
    }
}
