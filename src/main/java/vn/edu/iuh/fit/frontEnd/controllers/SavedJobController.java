package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.SavedJob;
import vn.edu.iuh.fit.backEnd.repositories.CandidateRepository;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;
import vn.edu.iuh.fit.backEnd.services.SavedJobService;

import java.util.List;

@Controller
public class SavedJobController {

    @Autowired
    private SavedJobService savedJobService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    // Xử lý nút LƯU
    @GetMapping("/save-job/{jobId}")
    public String saveJob(@PathVariable Long jobId, HttpSession session) {
        Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
        Job job = jobRepository.findById(jobId).orElse(null);

        if (candidate != null && job != null) {
            savedJobService.saveJob(candidate, job);
        }

        return "redirect:/Timkiemvieclam";
    }

    // Trang xem việc làm đã lưu
    @GetMapping("/vieclamdaluu")
    public String viewSavedJobs(Model model, HttpSession session) {
        Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");

        if (candidate == null) {
            return "redirect:/candidate_login"; // hoặc route đăng nhập của bạn
        }

        List<SavedJob> savedJobs = savedJobService.getSavedJobs(candidate);
        model.addAttribute("savedJobs", savedJobs);
        return "vieclamdaluu"; // HTML hiển thị danh sách đã lưu
    }

    @PostMapping("/delete-saved/{id}")
    public String deleteSavedJob(@PathVariable Long id, HttpSession session) {
        Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");

        if (candidate != null) {
            savedJobService.deleteByIdAndCandidate(id, candidate);
        }

        return "redirect:/vieclamdaluu";
    }
}
