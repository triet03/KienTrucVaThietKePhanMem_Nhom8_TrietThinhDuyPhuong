//package vn.edu.iuh.fit.frontEnd.controllers;
//
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import vn.edu.iuh.fit.backEnd.models.Candidate;
//import vn.edu.iuh.fit.backEnd.services.CandidateService;
//
//@Controller
//public class CandidateLoginController {
//
//    @Autowired
//    private CandidateService candidateService;
//
//    // Hiển thị form đăng ký
//    @GetMapping("/register")
//    public String showRegisterForm() {
//        return "candidate_register"; // Trả về file candidate_register.html
//    }
//
//    // Xử lý đăng ký người tìm việc
//    @PostMapping("/register/candidate")
//    public String processCandidateRegistration(@RequestParam String fullName,
//                                               @RequestParam String email,
//                                               @RequestParam String password) {
//        Candidate candidate = new Candidate();
//        candidate.setFullName(fullName);
//        candidate.setEmail(email);
//        candidate.setPassword(password); // có thể mã hóa nếu cần
//
//        candidateService.saveCandidate(candidate);
//        System.out.println("✅ Người tìm việc đăng ký: " + fullName + " - " + email);
//
//        return "redirect:/login/candidate";
//    }
//
//    // Hiển thị form đăng nhập
//    @GetMapping("/login/candidate")
//    public String showLoginForm() {
//        return "candidate_login"; // Trả về file candidate_login.html
//    }
//
////    // Xử lý đăng nhập người tìm việc
////    @PostMapping("/login/candidate")
////    public String processCandidateLogin(@RequestParam String email,
////                                        @RequestParam String password,
////                                        HttpSession session,
////                                        Model model) {
////        Candidate candidate = candidateService.findByEmail(email);
////
////        if (candidate != null && candidate.getPassword().equals(password)) {
////            session.setAttribute("loggedInCandidate", candidate);
////            System.out.println("✅ Đăng nhập thành công: " + email);
////            return "redirect:/jobs"; // ✅ Chuyển đến trang job-list.html
////        }
////
////        model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
////        return "candidate_login";
////    }
//
//    // ✅ Xử lý đăng nhập và chuyển đến dashboard
//    @PostMapping("/login/candidate")
//    public String processCandidateLogin(@RequestParam String email,
//                                        @RequestParam String password,
//                                        HttpSession session,
//                                        Model model) {
//        Candidate candidate = candidateService.findByEmail(email);
//
//        if (candidate != null && candidate.getPassword().equals(password)) {
//            session.setAttribute("loggedInCandidate", candidate);
//            System.out.println("✅ Đăng nhập thành công: " + email);
//            return "redirect:/dashboardCandidate"; // 🔄 CHUYỂN ĐẾN DASHBOARD
//        }
//
//        model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
//        return "candidate_login";
//    }
//
//    // ✅ Xử lý đăng xuất
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login/candidate";
//    }
//}
    package vn.edu.iuh.fit.frontEnd.controllers;

    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import vn.edu.iuh.fit.backEnd.models.Address;
    import vn.edu.iuh.fit.backEnd.models.Candidate;
    import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
    import vn.edu.iuh.fit.backEnd.repositories.CandidateSkillRepository;
    import vn.edu.iuh.fit.backEnd.services.CandidateService;
    import vn.edu.iuh.fit.backEnd.services.JobService;

    import java.util.List;

@Controller
    public class CandidateLoginController {

        @Autowired
        private CandidateService candidateService;
        @Autowired
        private CandidateSkillRepository candidateSkillRepository;
        @Autowired
        private JobService jobService;

        // -------------------- HIỂN THỊ FORM ĐĂNG KÝ --------------------
        @GetMapping("/register")
        public String showRegisterForm() {
            return "candidate_register"; // Trả về file candidate_register.html
        }

        // -------------------- XỬ LÝ ĐĂNG KÝ --------------------
        @PostMapping("/register/candidate")
        public String processCandidateRegistration(@RequestParam String fullName,
                                                   @RequestParam String email,
                                                   @RequestParam String password) {
            Candidate candidate = new Candidate();
            candidate.setFullName(fullName);
            candidate.setEmail(email);
            candidate.setPassword(password); // Có thể mã hóa bằng BCrypt

            candidateService.saveCandidate(candidate);
            System.out.println("✅ Người tìm việc đăng ký: " + fullName + " - " + email);

            return "redirect:/login/candidate";
        }

        // -------------------- HIỂN THỊ FORM ĐĂNG NHẬP --------------------
        @GetMapping("/login/candidate")
        public String showLoginForm() {
            return "candidate_login"; // Trả về file candidate_login.html
        }

        // -------------------- XỬ LÝ ĐĂNG NHẬP --------------------
        @PostMapping("/login/candidate")
        public String processCandidateLogin(@RequestParam String email,
                                            @RequestParam String password,
                                            HttpSession session,
                                            Model model) {
            Candidate candidate = candidateService.findByEmail(email);

            if (candidate != null && candidate.getPassword().equals(password)) {
                session.setAttribute("loggedInCandidate", candidate);
                System.out.println("✅ Đăng nhập thành công: " + email);
                return "redirect:/dashboardCandidate";
            }

            model.addAttribute("error", "Email hoặc mật khẩu không đúng.");
            return "candidate_login";
        }

        // -------------------- HIỂN THỊ DASHBOARD ỨNG VIÊN --------------------
        @GetMapping("/dashboardCandidate")
        public String showDashboardCandidate(HttpSession session, Model model) {
            Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
            if (candidate == null) {
                return "redirect:/login/candidate";
            }

            model.addAttribute("loggedInCandidate", candidate);
          // model.addAttribute("jobs", jobService.findAll());

            return "dashboard-candidate"; // Trả về file dashboard-candidate.html
        }


        @GetMapping("/tranghosonguoidung")
        public String showProfile(HttpSession session, Model model) {
            Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
            if (candidate == null) return "redirect:/login/candidate";

            model.addAttribute("candidate", candidate);
            return "tranghosonguoidung" ; // 👉 Trả về file bạn vừa gửi
        }

//        // -------------------- HIỂN THỊ FORM CẬP NHẬT HỒ SƠ --------------------
//        @GetMapping("/hoso-candidate")
//        public String showUpdateCandidateForm(HttpSession session, Model model) {
//            Long candidateId = (Long) session.getAttribute("loggedInCandidateId");
//            if (candidateId == null) {
//                return "redirect:/login/candidate";
//            }
//            return "update-candidate";
//        }
// -------------------- FORM CẬP NHẬT THÔNG TIN --------------------
@GetMapping("/update-candidate")
public String showUpdateForm(HttpSession session, Model model) {
    Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
    if (candidate == null) return "redirect:/login/candidate";

    Candidate fullCandidate = candidateService.getCandidateById(candidate.getCanId());
    model.addAttribute("candidate", fullCandidate);
    return "uupdate-candidate";
}

    @PostMapping("/candidates/update/{id}")
    public String updateCandidate(@PathVariable("id") Long id,
                                  @ModelAttribute("candidate") Candidate updatedCandidate,
                                  HttpSession session) {

        // Lấy dữ liệu ứng viên hiện tại từ DB
        Candidate existing = candidateService.getCandidateById(id);
        if (existing == null) {
            return "redirect:/login/candidate";
        }

        // ✅ Cập nhật thông tin cá nhân
        existing.setFullName(updatedCandidate.getFullName());
        existing.setDob(updatedCandidate.getDob());
        existing.setPhone(updatedCandidate.getPhone());
        existing.setEmail(updatedCandidate.getEmail());
        existing.setSummary(updatedCandidate.getSummary()); // ⚠️ BẮT BUỘC có dòng này!

        // ✅ Cập nhật địa chỉ
        if (updatedCandidate.getAddress() != null) {
            Address newAddr = updatedCandidate.getAddress();
            Address currentAddr = existing.getAddress();

            if (currentAddr == null) {
                currentAddr = new Address(); // tạo mới nếu chưa có
            }

            currentAddr.setStreet(newAddr.getStreet());
            currentAddr.setCity(newAddr.getCity());
            currentAddr.setZipcode(newAddr.getZipcode());
            currentAddr.setCountry(newAddr.getCountry());

            existing.setAddress(currentAddr);
        }

        // ✅ Lưu lại vào DB
        Candidate savedCandidate = candidateService.save(existing);

        // Cập nhật session
        session.setAttribute("loggedInCandidate", savedCandidate);

        // Thông báo thành công
        session.setAttribute("updateSuccess", "Thông tin đã được cập nhật thành công!");

        // Chuyển hướng về hồ sơ
        return "redirect:/hoso-cua-ban";
    }


    //--------Hien thi ho so cua ban--------///
        @GetMapping("/hoso-cua-ban")
        public String showHoSoCuaBan(HttpSession session, Model model) {
            // Lấy candidate từ session
            Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
            if (candidate == null) {
                System.out.println("ERROR: loggedInCandidate is null in session");
                return "redirect:/login/candidate";
            }
            // Kiểm tra candidate có null không
            System.out.println("Candidate: " + candidate.toString());

            // Lấy danh sách kỹ năng dựa trên canId
            List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateCanId(candidate.getCanId());
            if (candidateSkills == null || candidateSkills.isEmpty()) {
                System.out.println("No skills found for candidate ID: " + candidate.getCanId());
            } else {
                System.out.println("Skills found: " + candidateSkills.size());
            }

            // Thêm candidate và candidateSkills vào model
            model.addAttribute("candidate", candidate);
            model.addAttribute("candidateSkills", candidateSkills);

            return "ho-so-cua-ban";
        }



        /// ----mk----//
        @GetMapping("/change-password")
        public String showChangePasswordForm() {
            return "change-password";
        }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
        if (candidate == null) {
            return "redirect:/login/candidate";
        }

        if (!candidate.getPassword().equals(oldPassword)) {
            model.addAttribute("error", "Mật khẩu cũ không đúng.");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Xác nhận mật khẩu không khớp.");
            return "change-password";
        }

        candidate.setPassword(newPassword);
        candidateService.saveCandidate(candidate);

        model.addAttribute("success", "Đổi mật khẩu thành công.");
        return "change-password";
    }

    @GetMapping("/user-guide")
    public String userGuide() {
        return "user-guide"; // tên file HTML trong /templates mà không có phần mở rộng
    }
        // -------------------- XỬ LÝ ĐĂNG XUẤT --------------------
        @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            System.out.println("👋 Đăng xuất thành công");
            return "redirect:/login/candidate";
        }
    }
