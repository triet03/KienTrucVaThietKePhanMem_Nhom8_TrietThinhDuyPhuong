package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.*;

import java.util.List;
@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {
//
//    // Sửa phương thức tìm kiếm công việc theo kỹ năng
    @Query("SELECT js FROM JobSkill js WHERE js.job.jobId = :jobId")
    List<JobSkill> findByJobId(Long jobId); // Sử dụng jobId thay vì id
//    
//    // Tìm các công việc yêu cầu một kỹ năng cụ thể
    @Query("SELECT j FROM Job j JOIN JobSkill js ON j.jobId = js.job.jobId WHERE js.skill = :skill")
    List<Job> findJobsBySkill(Skill skill);  // Cập nhật đúng tên trường trong Job

//    List<JobSkill> findByJobId(Long jobId);
//    List<JobSkill> findBySkill(Skill skill);
}
