package vn.edu.iuh.fit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EnableCaching
public class NguyenBaMinhTrietLabWeek05Application {

    public static void main(String[] args) {
        SpringApplication.run(NguyenBaMinhTrietLabWeek05Application.class, args);
    }

}
