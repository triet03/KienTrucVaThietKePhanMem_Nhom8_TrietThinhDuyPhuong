package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expId;
    private Date fromDate;
    private Date toDate;
    private String company;
    private String workDesc;

    @ManyToOne
    @JoinColumn(name = "can_id")
    private Candidate candidate;
    // getters and setters
    
    
}