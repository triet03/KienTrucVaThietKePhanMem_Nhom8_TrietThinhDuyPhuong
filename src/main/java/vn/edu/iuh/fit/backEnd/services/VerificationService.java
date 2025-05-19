package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationService {

    @Autowired
    private JavaMailSender mailSender;

    private static final int EXPIRE_MINUTES = 5;

    private static class VerificationCode {
        String code;
        LocalDateTime expireAt;

        VerificationCode(String code, LocalDateTime expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
    }

    private Map<String, VerificationCode> codeStorage = new HashMap<>();

    public String generateAndSendCode(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6 chữ số
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(EXPIRE_MINUTES);
        codeStorage.put(email, new VerificationCode(code, expireAt));
        sendEmail(email, code);
        return code;
    }

    public String getVerificationCode(String email) {
        VerificationCode entry = codeStorage.get(email);
        if (entry == null) return null;
        if (LocalDateTime.now().isAfter(entry.expireAt)) {
            codeStorage.remove(email); // Hết hạn thì xóa luôn
            return null;
        }
        return entry.code;
    }

    public void removeCode(String email) {
        codeStorage.remove(email);
    }

    private void sendEmail(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Mã xác thực đăng ký");
            message.setText("Mã xác thực của bạn là: " + code + ". Mã có hiệu lực trong 5 phút.");
            mailSender.send(message);
            System.out.println("✅ Gửi mã: " + code + " đến " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email:");
            e.printStackTrace();
        }
    }
}
