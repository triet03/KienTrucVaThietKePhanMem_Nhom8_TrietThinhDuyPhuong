package vn.edu.iuh.fit.frontEnd.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Controller
public class HomeCompanyController {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @GetMapping("/homeCompany")
    public String homeCompany(Model model) {
        RateLimiter limiter = rateLimiterRegistry.rateLimiter("homeCompanyLimiter");
        System.out.println("RateLimiter config: " + limiter.getRateLimiterConfig());
        try {
            return RateLimiter.decorateSupplier(limiter, () -> {
                return "company";
            }).get();
        } catch (RequestNotPermitted ex) {
            System.out.println("Rate limit exceeded: " + ex.getMessage());
            model.addAttribute("error", "⚠️ Đã vượt quá số lần truy cập. Vui lòng thử lại sau 1 phút.");
            return "company";
        }
    }
}
