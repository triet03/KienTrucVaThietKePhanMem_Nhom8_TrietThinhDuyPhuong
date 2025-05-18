package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.enums.JobStatus;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.Skill;
import vn.edu.iuh.fit.backEnd.services.CandidateRecommendationService;
import vn.edu.iuh.fit.backEnd.services.CompanyService;
import vn.edu.iuh.fit.backEnd.services.JobService;
import vn.edu.iuh.fit.backEnd.services.SkillService;
import vn.edu.iuh.fit.backEnd.models.JobSkill;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private CandidateRecommendationService candidateRecommendationService;

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("job", new Job());
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("skills", skillService.getAllSkills());
        return "create-job";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute Job job, @RequestParam List<Long> skillIds) {
        job.setStatus(JobStatus.valueOf("PENDING")); // Đặt trạng thái là "PENDING"
        jobService.createJobWithSkills(job, skillIds);  // Lưu công việc với kỹ năng
        return "redirect:/jobs/pending";  // Chuyển hướng đến trang công việc đang chờ duyệt
    }


    @GetMapping
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobService.getApprovedJobs());
        return "job-list";
    }

    //    @GetMapping("/recommendations/{id}")
//    public String showCandidateRecommendations(@PathVariable("id") long id, Model model) {
//        model.addAttribute("candidates", candidateRecommendationService.recommendCandidatesForJob(id));
//        return "candidate-recommendations";
//    }
    @GetMapping("/recommendations/{id}")
    public String showCandidateRecommendations(@PathVariable("id") long id, Model model) {
        model.addAttribute("candidates", candidateRecommendationService.recommendCandidatesForJob(id));
        model.addAttribute("jobId", id);
        return "candidate-recommendations";
    }

    @PostMapping("/send-email/{candidateId}")
    @ResponseBody
    public String sendEmailToCandidate(@PathVariable Long candidateId, @RequestParam Long jobId) {
        candidateRecommendationService.sendEmailToCandidate(candidateId, jobId);
        return "Email sent successfully!";
    }

    @GetMapping("/{id}")
    public String showJobDetails(@PathVariable("id") long id, Model model) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return "redirect:/jobs"; // Nếu công việc không tồn tại, quay lại danh sách
        }

        // Lấy danh sách kỹ năng từ JobSkill
        List<Skill> skills = job.getJobSkills().stream()
                .map(JobSkill::getSkill)
                .toList();

        model.addAttribute("job", job);
        model.addAttribute("skills", skills);

        return "job-details"; // Đảm bảo tên view trùng với file HTML
    }

    @GetMapping("/pending")
    public String getPendingJobs(Model model) {
        List<Job> pendingJobs = jobService.getPendingJobs();  // Lấy công việc đang chờ duyệt
        model.addAttribute("jobs", pendingJobs);
        return "Dangtintuyendung"; // Đảm bảo tên view là "Dangtintuyendung"
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long jobId, Model model) {
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            return "redirect:/jobs/pending"; // Nếu job không tồn tại, quay lại danh sách
        }
        model.addAttribute("job", job);
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("skills", skillService.getAllSkills());
        // Lấy danh sách skillId hiện tại của job
        List<Long> selectedSkillIds = job.getJobSkills().stream()
                .map(jobSkill -> jobSkill.getSkill().getSkillId())
                .toList();
        model.addAttribute("selectedSkillIds", selectedSkillIds);
        return "edit-job"; // Tên template form chỉnh sửa
    }

    @PostMapping("/update")
    public String updateJob(@ModelAttribute Job job,
                            @RequestParam Long companyId,
                            @RequestParam(required = false) List<Long> skillIds) {
        // Gán công ty
        Company company = companyService.getCompanyById(companyId);
        job.setCompany(company);
        // Đặt trạng thái PENDING
        job.setStatus(JobStatus.PENDING);
        // Cập nhật job và kỹ năng
        jobService.updateJobWithSkills(job.getJobId(), job, skillIds != null ? skillIds : new ArrayList<>());
        return "redirect:/jobs/pending";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable("id") Long jobId) {
        System.out.println("Xóa job ID: " + jobId); // Log để debug
        jobService.deleteJob(jobId);
        return "redirect:/jobs/pending";
    }


}