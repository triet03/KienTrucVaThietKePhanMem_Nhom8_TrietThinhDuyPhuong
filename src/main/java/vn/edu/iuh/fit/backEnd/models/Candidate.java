package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Setter
@Getter
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canId;
    private String fullName;
    private String email;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @ManyToOne(cascade = CascadeType.ALL)  // Thêm CascadeType.ALL để tự động lưu Address khi lưu Candidate
    @JoinColumn(name = "address_id")
    private Address address;

    // getters and setters

	public Candidate() {
		super();
		// TODO Auto-generated constructor stub
	}

    @Override
	public String toString() {
		return "Candidate [canId=" + canId + ", fullName=" + fullName + ", email=" + email + ", phone=" + phone
				+ ", dob=" + dob + ", address=" + address + "]";
	}
    
    
}