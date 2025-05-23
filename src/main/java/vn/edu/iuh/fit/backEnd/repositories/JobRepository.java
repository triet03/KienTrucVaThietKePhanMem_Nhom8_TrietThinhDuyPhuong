package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.Skill;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
//    @Query("SELECT j FROM Job j JOIN JobSkill js ON j.jobId = js.job.jobId WHERE js.skill = :skill")
//    List<Job> findJobsBySkill(Skill skill);

    // Truy vấn tìm Job theo Skill
    @Query("SELECT j FROM Job j JOIN JobSkill js ON j.jobId = js.job.jobId WHERE js.skill = :skill")
    List<Job> findJobsBySkill(Skill skill);

    @Query("SELECT j FROM Job j " +
            "WHERE (:keyword IS NULL OR LOWER(j.jobName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:minSalary IS NULL OR j.salary >= :minSalary) " +
            "AND (:maxSalary IS NULL OR j.salary <= :maxSalary) " +
            "AND (:city IS NULL OR LOWER(j.company.address.city) LIKE LOWER(CONCAT('%', :city, '%')))")
    List<Job> searchJobs(String keyword, Integer minSalary, Integer maxSalary, String city);

//    @Query("SELECT j.jobName FROM Job j WHERE LOWER(j.jobName) LIKE LOWER(:keyword)")
//    List<String> findJobNamesByKeyword(@Param("keyword") String keyword);
    @Query("SELECT j.jobName FROM Job j WHERE LOWER(j.jobName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findJobNamesByKeyword(@Param("keyword") String keyword);



}
