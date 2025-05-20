package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeCandidateController {

    @GetMapping("/homeCandidate")
    public String homeCandidate() {
        return "index"; // Tên file HTML trang Nhà tuyển dụng
    }
    @GetMapping("/Timkiemvieclam")
    public String timKiemViecLam() {
        return "Timkiemvieclam"; // Spring sẽ tìm file templates/Timkiemvieclam.html
    }

}
