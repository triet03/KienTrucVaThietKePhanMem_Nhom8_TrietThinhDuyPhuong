package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.ApplicationDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ungVien")  // Đặt prefix dùng 1 lần ở đây
public class ApplicationController {

    private List<ApplicationDTO> applications = new ArrayList<>();

    @PostMapping("/submitApplication")
    public String submitApplication(@RequestParam String fullName,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String experience,
                                    @RequestParam String skills,
                                    @RequestParam String coverLetter,
                                    @RequestParam String position,
                                    @RequestParam String company) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setExperience(experience);
        dto.setSkills(skills);
        dto.setCoverLetter(coverLetter);
        dto.setPosition(position);
        dto.setCompany(company);
        dto.setDateApplied(LocalDate.now().toString());
        dto.setStatus("Đã nộp");

        applications.add(dto);

        return "redirect:/ungVien/hoSoUngTuyen";
    }

    @GetMapping("/hoSoUngTuyen")
    public String viewApplications(Model model) {
        model.addAttribute("applications", applications);
        return "hoSoUngTuyen"; // tương ứng với hoSoUngTuyen.html
    }

    @GetMapping("/hoSoUngTuyen/{index}")
    public String viewApplicationDetail(@PathVariable int index, Model model) {
        if (index >= 0 && index < applications.size()) {
            model.addAttribute("application", applications.get(index));
        }
        return "application-detail";
    }
}
