package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeCompanyController {
    @GetMapping("/homeCompany")
    public String homeCompany() {
        return "company"; // Tên file HTML trang Nhà tuyển dụng
    }
}
