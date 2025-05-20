package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class CandidateSkillId implements Serializable {
    private Long canId;
    private Long skillId;
    // getters and setters, equals() and hashCode()
	public CandidateSkillId() {
		super();
		// TODO Auto-generated constructor stub
	}


}