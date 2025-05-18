package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@Entity
public class Company implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comId;

    private String about;
    private String compName;
    private String email;
    private String phone;
    private String webUrl;
    private String password;
    private boolean emailVerified = false;  // Trạng thái xác thực
    private String verificationToken;       // Mã xác thực email

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address")
    private Address address;

    public Company() {
        super();
    }

    // ✅ 1. Cấp quyền truy cập (hiện tại chưa có vai trò cụ thể)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Không có quyền cụ thể
    }

    @Override
    public String getPassword() {
        return "";
    }

    // ✅ 2. Sử dụng email làm username cho đăng nhập
    @Override
    public String getUsername() {
        return email;
    }

    // ✅ 3. Các phương thức kiểm tra trạng thái tài khoản
    @Override
    public boolean isAccountNonExpired() {
        return true; // Tài khoản không bị hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Tài khoản không bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mật khẩu không hết hạn
    }

    @Override
    public boolean isEnabled() {
        return emailVerified; // Chỉ kích hoạt nếu email đã xác thực
    }
}
