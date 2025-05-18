package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.iuh.fit.backEnd.models.Application;
import vn.edu.iuh.fit.backEnd.services.ApplicationService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/ungVien")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ResourceLoader resourceLoader; // Thêm để lấy đường dẫn tài nguyên

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/nop-ho-so")
    public String submitApplication(
            @RequestParam("position") String position,
            @RequestParam("company") String company,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("experience") String experience,
            @RequestParam("skills") String skills,
            @RequestParam("coverLetter") String coverLetter,
            @RequestParam("dateApplied") String dateApplied,
            @RequestParam("status") String status,
            @RequestParam("cvImage") MultipartFile cvImage,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String fileName = null;
            if (!cvImage.isEmpty()) {
                fileName = UUID.randomUUID().toString() + "_" + cvImage.getOriginalFilename();
                // Lấy đường dẫn tuyệt đối của thư mục uploads
                String absoluteUploadPath = resourceLoader.getResource("classpath:static/uploads/").getFile().getAbsolutePath();
                File destFile = new File(absoluteUploadPath + File.separator + fileName);
                destFile.getParentFile().mkdirs(); // Tạo thư mục nếu chưa tồn tại
                cvImage.transferTo(destFile);
                System.out.println("File saved at: " + destFile.getAbsolutePath());
            }

            Application app = new Application();
            app.setPosition(position);
            app.setCompany(company);
            app.setFullName(fullName);
            app.setEmail(email);
            app.setPhone(phone);
            app.setExperience(experience);
            app.setSkills(skills);
            app.setCoverLetter(coverLetter);
            app.setDateApplied(dateApplied);
            app.setStatus(status);
            app.setCvFileName(fileName);

            applicationService.save(app);
            System.out.println("Application saved: " + app);

            redirectAttributes.addFlashAttribute("message", "Hồ sơ đã được gửi thành công!");
            return "redirect:/jobs"; // Đã sửa từ /job-list
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên file CV: " + e.getMessage());
            return "redirect:/jobs";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi gửi hồ sơ: " + e.getMessage());
            return "redirect:/jobs";
        }
    }
}