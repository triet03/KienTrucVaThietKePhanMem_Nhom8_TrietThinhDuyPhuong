package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "job_skill")
public class JobSkill {
    // Getters & Setters
    @EmbeddedId
    private JobSkillId id;

    @ManyToOne
    @MapsId("jobId") // Liên kết đến Job thông qua jobId
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @MapsId("skillId") // Liên kết đến Skill thông qua skillId
    @JoinColumn(name = "skill_id")
    private Skill skill;

    // Constructor
    public JobSkill() {}

    public JobSkill(Job job, Skill skill) {
        this.id = new JobSkillId(job.getJobId(), skill.getSkillId());
        this.job = job;
        this.skill = skill;
    }

}
