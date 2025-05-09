package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.iuh.fit.backEnd.enums.JobStatus;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/jobs")
public class AdminJobController {

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public String viewPendingJobs(Model model) {
        List<Job> pendingJobs = jobRepository.findByStatus(JobStatus.PENDING);
        model.addAttribute("pendingJobs", pendingJobs);
        return "admin_jobs";
    }

    @PostMapping("/approve/{id}")
    public String approveJob(@PathVariable Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job != null) {
            job.setStatus(JobStatus.APPROVED);
            jobRepository.save(job);
        }
        return "redirect:/admin/jobs";
    }

    @PostMapping("/reject/{id}")
    public String rejectJob(@PathVariable Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job != null) {
            job.setStatus(JobStatus.REJECTED);
            jobRepository.save(job);
        }
        return "redirect:/admin/jobs";
    }
}

