package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "candidate", indexes = {
        @Index(name = "idx_email", columnList = "email")
})
public class Candidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canId;
    private String fullName;

    @Column(unique = true)
    private String email;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Candidate() {
        super();
    }

    @Override
    public String toString() {
        return "Candidate [canId=" + canId + ", fullName=" + fullName + ", email=" + email + ", phone=" + phone
                + ", dob=" + dob + ", address=" + address + "]";
    }
}
