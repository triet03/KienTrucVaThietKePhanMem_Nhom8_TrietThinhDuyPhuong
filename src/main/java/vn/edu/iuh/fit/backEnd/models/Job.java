package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.iuh.fit.backEnd.enums.JobStatus;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobName;
    private String jobDesc;
    private int salary;
    @ManyToOne
    @JoinColumn(name = "company")
    private Company company;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING; // mặc định là chờ duyệt
    // getters and setters

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSkill> jobSkills;

	public Job() {
		super();
		// TODO Auto-generated constructor stub
	}


}
