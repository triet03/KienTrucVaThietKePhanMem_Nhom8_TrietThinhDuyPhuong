package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    private Byte skillType;
    private String skillName;
    private String skillDesc;
    // getters and setters
	public Skill() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Skill(Long skillId, Byte skillType, String skillName, String skillDesc) {
		super();
		this.skillId = skillId;
		this.skillType = skillType;
		this.skillName = skillName;
		this.skillDesc = skillDesc;
	}

    @Override
	public String toString() {
		return "Skill [skillId=" + skillId + ", skillType=" + skillType + ", skillName=" + skillName + ", skillDesc="
				+ skillDesc + "]";
	}
    
    
}
