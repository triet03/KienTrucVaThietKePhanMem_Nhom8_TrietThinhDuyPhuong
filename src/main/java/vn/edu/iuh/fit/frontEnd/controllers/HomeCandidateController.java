package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.services.CompanyService;
import vn.edu.iuh.fit.backEnd.services.JobService;

import java.util.List;
import java.util.Map;

@Controller
public class HomeCandidateController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;

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

        // ✅ Kiểm tra giá trị lương âm
        if ((minSalary != null && minSalary < 0) || (maxSalary != null && maxSalary < 0)) {
            model.addAttribute("error", "Lương không được nhỏ hơn 0");
            model.addAttribute("jobs", List.of()); // Trả về danh sách rỗng
        } else {
            List<Job> jobs = jobService.searchJobs(keyword, minSalary, maxSalary, city);
            model.addAttribute("jobs", jobs);
        }

        List<String> cities = companyService.getAllCities();
        model.addAttribute("cities", cities);
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
}
