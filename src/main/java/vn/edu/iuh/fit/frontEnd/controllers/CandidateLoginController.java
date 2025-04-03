package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CandidateLoginController {

    @GetMapping("/register")
    public String showRegisterForm() {
        return "candidate_register"; // Trả về file candidate_register.html
    }


    @PostMapping("/register/candidate")
    public String processCandidateRegistration(@RequestParam String fullName,
                                               @RequestParam String email,
                                               @RequestParam String password) {
        // Xử lý lưu vào database
        System.out.println("Người tìm việc đăng ký: " + fullName + " - " + email);
        return "redirect:/login/candidate";
    }

    @GetMapping("/login/candidate")
    public String showLoginForm() {
        return "candidate_login"; // Trả về file candidate_login.html
    }

    @PostMapping("/login/candidate")
    public String processCandidateLogin(@RequestParam String email,
                                        @RequestParam String password) {
        // Xử lý đăng nhập (check database)
        System.out.println("Người tìm việc đăng nhập: " + email);
        return "redirect:/home";
    }
}
