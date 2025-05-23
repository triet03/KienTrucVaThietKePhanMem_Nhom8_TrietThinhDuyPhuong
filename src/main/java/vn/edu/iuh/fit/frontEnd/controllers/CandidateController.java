    package vn.edu.iuh.fit.frontEnd.controllers;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;
    import vn.edu.iuh.fit.backEnd.models.Candidate;
    import vn.edu.iuh.fit.backEnd.services.CandidateService;
    import vn.edu.iuh.fit.backEnd.services.JobRecommendationService;
    import vn.edu.iuh.fit.backEnd.services.SkillService;

    import java.util.List;

    @Controller
    @RequestMapping("/candidates")
    public class CandidateController {
        @Autowired
        private CandidateService candidateService;
        @Autowired
        private JobRecommendationService jobRecommendationService;
        @Autowired
        private SkillService skillService;

        @GetMapping
        public String getAllCandidates(Model model) {
            List<Candidate> candidates = candidateService.getAllCandidates();
            model.addAttribute("candidates", candidates);
            return "candidate-list";
        }


        @GetMapping("/new")
        public String showCreateForm(Model model) {
            model.addAttribute("candidate", new Candidate());
            return "create-candidate";
        }

        @PostMapping("/save")
        public String saveCandidate(@ModelAttribute Candidate candidate) {
            candidateService.createCandidate(candidate);
            return "redirect:/candidates";
        }

        @GetMapping("/edit/{id}")
        public String showUpdateForm(@PathVariable("id") long id, Model model) {
            Candidate candidate = candidateService.getCandidateById(id);
            model.addAttribute("candidate", candidate);
            return "uupdate-candidate";
        }

    //    @PostMapping("/update/{id}")
    //    public String updateCandidate(@PathVariable("id") long id, @ModelAttribute Candidate candidate) {
    //        candidateService.updateCandidate(id, candidate);
    //        return "redirect:/candidates";
    //    }

        @GetMapping("/delete/{id}")
        public String deleteCandidate(@PathVariable("id") long id) {
            candidateService.deleteCandidate(id);
            return "redirect:/candidates";
        }
        @GetMapping("/recommendations/{id}")
        public String showJobRecommendations(@PathVariable("id") long id, Model model) {
            model.addAttribute("jobs", jobRecommendationService.recommendJobsForCandidate(id));
            return "job-recommendations";
        }

        @GetMapping("/skills/{id}")
        public String showSkillRecommendations(@PathVariable("id") long id, Model model) {
            model.addAttribute("skills", skillService.recommendSkillsForCandidate(id));
            return "skill-recommendations";
        }
        @GetMapping("/profile/{id}")
        public String viewProfile(@PathVariable("id") long id, Model model) {
            Candidate candidate = candidateService.getCandidateById(id);
            // Nếu entity Candidate đã mapping với Address & CandidateSkill rồi thì chỉ cần add
            model.addAttribute("candidate", candidate);

            // Nếu chưa mapping kỹ năng, bạn có thể viết hàm getSkillByCandidateId trong SkillService
            model.addAttribute("skills", skillService.getSkillsByCandidateId(id));

            return "TrangHoSoNguoiDung"; // Tên file html bạn dùng để hiển thị profile
        }

        @PostMapping("/change-password/{id}")
        public String changePassword(
                @PathVariable("id") Long id,
                @RequestParam("oldPassword") String oldPassword,
                @RequestParam("newPassword") String newPassword,
                @RequestParam("confirmPassword") String confirmPassword,
                RedirectAttributes redirectAttributes
        ) {
            Candidate candidate = candidateService.getCandidateById(id);
            if (candidate == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tài khoản.");
                return "redirect:/candidates/profile/" + id;
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
                return "redirect:/candidates/profile/" + id;
            }
            if (!candidateService.checkPassword(candidate, oldPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không đúng.");
                return "redirect:/candidates/profile/" + id;
            }
            candidateService.updatePassword(candidate, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
            return "redirect:/candidates/profile/" + id;
        }

        @PostMapping("/update/{id}")
        public String updateCandidate(@PathVariable("id") Long id, @ModelAttribute Candidate candidate, RedirectAttributes ra) {
            candidateService.updateCandidate(id, candidate);
            ra.addFlashAttribute("successMessage", "Cập nhật thành công!");
            // Nếu dùng id:
            return "redirect:/candidates/profile/" + id;

        }

        @GetMapping("/hoso-cua-ban/{id}")
        public String userProfileById(@PathVariable("id") Long id, Model model) {
            Candidate candidate = candidateService.getCandidateById(id);
            model.addAttribute("candidate", candidate);
            model.addAttribute("candidateSkills", candidate.getCandidateSkills());
            return "ho-so-cua-ban";
        }

    }