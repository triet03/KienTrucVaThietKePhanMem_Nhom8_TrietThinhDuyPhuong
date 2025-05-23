
    package vn.edu.iuh.fit.frontEnd.controllers;

    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

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
//        @GetMapping("/dashboardCandidate")
//        public String showDashboardCandidate(HttpSession session, Model model) {
//            Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
//            if (candidate == null) {
//                return "redirect:/login/candidate";
//            }
//
//            model.addAttribute("loggedInCandidate", candidate);
//          // model.addAttribute("jobs", jobService.findAll());
//
//            return "dashboard-candidate"; // Trả về file dashboard-candidate.html
//        }


        @GetMapping("/tranghosonguoidung")
        public String showProfile(HttpSession session, Model model) {
            Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
            if (candidate == null) return "redirect:/login/candidate";

            model.addAttribute("candidate", candidate);
            return "tranghosonguoidung" ; // 👉 Trả về file bạn vừa gửi
        }

        // -------------------- HIỂN THỊ FORM CẬP NHẬT HỒ SƠ --------------------
        @GetMapping("/hoso-candidate")
        public String showUpdateCandidateForm(HttpSession session, Model model) {
            Long candidateId = (Long) session.getAttribute("loggedInCandidateId");
            if (candidateId == null) {
                return "redirect:/login/candidate";
            }
            return "update-candidate";
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




        // -------------------- XỬ LÝ ĐĂNG XUẤT --------------------
        @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            System.out.println("👋 Đăng xuất thành công");
            return "redirect:/login/candidate";
        }
    }
