package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.enums.JobStatus;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.Skill;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
//    @Query("SELECT j FROM Job j JOIN JobSkill js ON j.jobId = js.job.jobId WHERE js.skill = :skill")
//    List<Job> findJobsBySkill(Skill skill);

    // Truy vấn tìm Job theo Skill
    @Query("SELECT j FROM Job j JOIN JobSkill js ON j.jobId = js.job.jobId WHERE js.skill = :skill")
    List<Job> findJobsBySkill(Skill skill);
    List<Job> findByStatus(JobStatus status); // thêm dòng này

}
