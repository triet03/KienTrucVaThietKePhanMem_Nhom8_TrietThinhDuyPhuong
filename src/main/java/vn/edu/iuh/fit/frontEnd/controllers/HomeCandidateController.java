package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.Candidate; // ✅ THÊM DÒNG NÀY
import vn.edu.iuh.fit.backEnd.services.CompanyService;
import vn.edu.iuh.fit.backEnd.services.JobService;
import vn.edu.iuh.fit.backEnd.services.RecommendationService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Controller
public class HomeCandidateController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/homeCandidate")
    public String homeCandidate() {
        return "index"; // Tên file HTML trang Nhà tuyển dụng
    }

    @GetMapping("/Timkiemvieclam")
    public String searchJobs(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Integer minSalary,
                             @RequestParam(required = false) Integer maxSalary,
                             @RequestParam(required = false) String city,
                             Model model) {

        boolean isSearch = (keyword != null && !keyword.isBlank())
                || minSalary != null
                || maxSalary != null
                || (city != null && !city.isBlank());

        if ((minSalary != null && minSalary < 0) || (maxSalary != null && maxSalary < 0)) {
            model.addAttribute("error", "Lương không được nhỏ hơn 0");
            model.addAttribute("jobs", List.of()); // Trả về danh sách rỗng
        } else {
            // ✅ Ghi keyword vào file
            if (keyword != null && !keyword.isBlank()) {
                try (FileWriter fw = new FileWriter("search_keywords.txt", true)) {
                    fw.write(keyword.toLowerCase() + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // ✅ Tìm kiếm việc làm
            List<Job> jobs = jobService.searchJobs(keyword, minSalary, maxSalary, city);
            model.addAttribute("jobs", jobs);
        }

        List<String> cities = companyService.getAllCities();
        model.addAttribute("cities", cities);
        model.addAttribute("isSearch", isSearch);

        return "Timkiemvieclam";
    }


    @GetMapping("/Timkiemvieclam/suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam String keyword) {
        return jobService.findJobNamesByKeyword("%" + keyword.toLowerCase() + "%");
    }

    @Controller
    public class HomeController {
        @GetMapping("/")
        public String home() {
            return "index";
        }

        @GetMapping("/index")
        public String index() {
            return "index";
        }

    }
    @GetMapping("/dashboardCandidate")
    public String showDashboardCandidate(HttpSession session, Model model) {
        Candidate candidate = (Candidate) session.getAttribute("loggedInCandidate");
        if (candidate == null) {
            return "redirect:/login/candidate";
        }

        model.addAttribute("loggedInCandidate", candidate);


        List<Job> recommendedJobs = recommendationService.recommendJobsForCandidate();
        model.addAttribute("recommendedJobs", recommendedJobs);

        return "dashboard-candidate"; // Trả về file HTML
    }



}
