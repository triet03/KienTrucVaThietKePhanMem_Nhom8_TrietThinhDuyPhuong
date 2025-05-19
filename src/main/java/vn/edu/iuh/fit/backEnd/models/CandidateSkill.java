package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class CandidateSkill {
    @EmbeddedId
    private CandidateSkillId id;

    @ManyToOne
    @MapsId("canId")
    @JoinColumn(name = "can_id")
    private Candidate candidate;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private Byte skillLevel;
    private String moreInfos;
    // getters and setters
	public CandidateSkill() {
		super();
		// TODO Auto-generated constructor stub
	}


}