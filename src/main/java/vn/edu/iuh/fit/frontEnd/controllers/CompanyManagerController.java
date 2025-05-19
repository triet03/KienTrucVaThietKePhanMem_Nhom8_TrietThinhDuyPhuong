package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyManagerController {

    @GetMapping("/companyManager")
    public String companyManager() {
        return "QuanLyNhaTuyenDung"; // Tên file HTML
    }

    @GetMapping("/companyManager/dangKyGoiTin")
    public String companyDangKyGoiTin() {
        return "Dangkigoitin"; // Tên file HTML
    }

    @GetMapping("/companyManager/dangTinTuyenDung")
    public String companyDangTinTuyenDung() {
        return "Dangtintuyendung"; // Tên file HTML
    }

    @GetMapping("/companyManager/hoSoUngTuyen")
    public String companyHoSoUngTuyen() {
        return "HoSoUngTuyen"; // Tên file HTML
    }

    @GetMapping("/companyManager/quanLyNhaTuyenDung")
    public String companyQuanLyNhaTuyenDung() {
        return "QuanLyNhaTuyenDung"; // Tên file HTML
    }

    @GetMapping("/companyManager/quanLyThongTinNhaTuyenDung")
    public String companyQuanLyThongTinNhaTuyenDung() {
        return "QuanLyThongTinNhaTuyenDung"; // Tên file HTML
    }
}
