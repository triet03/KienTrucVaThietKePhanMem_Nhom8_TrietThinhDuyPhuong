package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.services.CompanyService;

@Controller
public class CompanyLoginController {

    @Autowired
    private CompanyService companyService;

    // -------------------- HIỂN THỊ FORM ĐĂNG NHẬP --------------------
    @GetMapping("/login/company")
    public String showLoginForm() {
        return "company_login"; // Trả về file company_login.html
    }

    // -------------------- XỬ LÝ ĐĂNG NHẬP --------------------
    @PostMapping("/login/company")
    public String processCompanyLogin(@RequestParam String email,
                                      @RequestParam String password,
                                      HttpSession session,
                                      Model model) {
        System.out.println("📩 Email: " + email);

        Company company = companyService.findByEmail(email);

        if (company != null) {
            System.out.println("✅ Tìm thấy công ty: " + company.getEmail());
            if (company.getPassword().equals(password)) {
                session.setAttribute("loggedInCompany", company);
                System.out.println("✅ Đăng nhập công ty thành công: " + email);
                return "redirect:/company";
            } else {
                System.out.println("❌ Sai mật khẩu cho: " + email);
            }
        } else {
            System.out.println("❌ Không tìm thấy công ty với email: " + email);
        }

        model.addAttribute("error", "❌ Email hoặc mật khẩu không đúng.");
        return "company_login";
    }

    // -------------------- HIỂN THỊ TRANG COMPANY --------------------
    @GetMapping("/company")
    public String showCompanyPage(HttpSession session, Model model) {
        Company company = (Company) session.getAttribute("loggedInCompany");
        if (company == null) {
            return "redirect:/login/company";
        }

        model.addAttribute("company", company);
        return "company"; // Trả về file company.html
    }

    // -------------------- XỬ LÝ ĐĂNG XUẤT --------------------
    @GetMapping("/logout/company")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("👋 Công ty đã đăng xuất");
        return "redirect:/login/company";
    }
}
