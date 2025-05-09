package vn.edu.iuh.fit.frontEnd.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

import java.util.List;

@Controller
@RequestMapping("/companyManager")
public class CompanyManagerController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public String companyManager(Model model, HttpSession session, Authentication authentication) {
        Company currentCompany = (Company) session.getAttribute("currentCompany");

        if (currentCompany == null) {
            currentCompany = getCurrentCompany(authentication);
            session.setAttribute("currentCompany", currentCompany); // lưu lại để lần sau không cần lại
        }

        List<Company> companyList = companyRepository.findAll();

        model.addAttribute("currentCompany", currentCompany);
        model.addAttribute("companyList", companyList);
        return "QuanLyNhaTuyenDung";
    }


    @GetMapping("/switchCompany/{id}")
    public String switchCompany(@PathVariable("id") long id, HttpSession session) {
        Company newCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id " + id));

        session.setAttribute("currentCompany", newCompany);  // Lưu vào session

        return "redirect:/companyManager";
    }


    @GetMapping("/quanLyThongTinNhaTuyenDung")
    public String companyQuanLyThongTinNhaTuyenDung(
            @RequestParam(value = "compName", required = false) String compName,
            Model model, Authentication authentication) {

        List<Company> allCompanies = companyRepository.findAll();
        Company selectedCompany;

        if (compName != null) {
            selectedCompany = companyRepository.findByCompName(compName);
        } else {
            selectedCompany = getCurrentCompany(authentication);
        }

        model.addAttribute("company", selectedCompany);
        model.addAttribute("allCompanies", allCompanies);
        model.addAttribute("selectedCompanyName", selectedCompany.getCompName());

        return "QuanLyThongTinNhaTuyenDung";
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@ModelAttribute Company company) {
        Company existing = companyRepository.findById(company.getComId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        existing.setCompName(company.getCompName());
        existing.setEmail(company.getEmail());
        existing.setPhone(company.getPhone());
        existing.setWebUrl(company.getWebUrl());
        existing.setAbout(company.getAbout());

        if (existing.getAddress() != null && company.getAddress() != null) {
            existing.getAddress().setAddId(company.getAddress().getAddId());
        }

        companyRepository.save(existing);

        return "redirect:/companyManager/quanLyThongTinNhaTuyenDung?compName=" + existing.getCompName();
    }

    @GetMapping("/dangKyGoiTin")
    public String companyDangKyGoiTin() {
        return "Dangkigoitin";
    }

    @GetMapping("/dangTinTuyenDung")
    public String companyDangTinTuyenDung() {
        return "Dangtintuyendung";
    }

    @GetMapping("/hoSoUngTuyen")
    public String companyHoSoUngTuyen() {
        return "HoSoUngTuyen";
    }

    // Helper để lấy công ty hiện tại từ authentication hoặc mặc định
    private Company getCurrentCompany(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Company) {
            return (Company) authentication.getPrincipal();
        }
        return companyRepository.findAll().stream().findFirst().orElse(null);
    }
}
