package vn.edu.iuh.fit.backEnd.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.enums.JobStatus;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.JobSkill;
import vn.edu.iuh.fit.backEnd.models.JobSkillId;
import vn.edu.iuh.fit.backEnd.models.Skill;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;
import vn.edu.iuh.fit.backEnd.repositories.JobSkillRepository;
import vn.edu.iuh.fit.backEnd.repositories.SkillRepository;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"jobs", "job"})
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobSkillRepository jobSkillRepository;

    @Cacheable(value = "jobs")
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)) // retry 2s mỗi lần, tối đa 3 lần
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1500))
    public Job getJobById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "jobs", allEntries = true),
            @CacheEvict(value = "job", key = "#result.jobId", condition = "#result != null")
    })
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1500))
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Caching(evict = {
            @CacheEvict(value = "jobs", allEntries = true),
            @CacheEvict(value = "job", key = "#result.jobId", condition = "#result != null")
    })
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public Job createJobWithSkills(Job job, List<Long> skillIds) {
        Job savedJob = jobRepository.save(job);
        for (Long skillId : skillIds) {
            Skill skill = skillRepository.findById(skillId).orElse(null);
            if (skill != null) {
                JobSkill jobSkill = new JobSkill();
                JobSkillId jobSkillId = new JobSkillId();
                jobSkillId.setJobId(savedJob.getJobId());
                jobSkillId.setSkillId(skillId);
                jobSkill.setId(jobSkillId);
                jobSkill.setJob(savedJob);
                jobSkill.setSkill(skill);
                jobSkillRepository.save(jobSkill);
            }
        }
        return savedJob;
    }

    @Caching(evict = {
            @CacheEvict(value = "jobs", allEntries = true),
            @CacheEvict(value = "job", key = "#id")
    })
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1500))
    public Job updateJob(Long id, Job jobDetails) {
        Job job = getJobById(id);
        if (job != null) {
            job.setJobName(jobDetails.getJobName());
            job.setJobDesc(jobDetails.getJobDesc());
            job.setCompany(jobDetails.getCompany());
            return jobRepository.save(job);
        }
        return null;
    }

    @Caching(evict = {
            @CacheEvict(value = "jobs", allEntries = true),
            @CacheEvict(value = "job", key = "#id")
    })
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1500))
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        if (job != null) {
            jobRepository.delete(job);
        }
    }

    public List<Job> getApprovedJobs() {
        return jobRepository.findByStatus(JobStatus.APPROVED);
    }

    public List<Job> getPendingJobs() {
        return jobRepository.findByStatus(JobStatus.PENDING);
    }

    public void updateJobStatus(Long jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job != null) {
            job.setStatus(status);
            jobRepository.save(job);
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "jobs", allEntries = true),
            @CacheEvict(value = "job", key = "#jobId")
    })
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1500))
    public Job updateJobWithSkills(Long jobId, Job jobDetails, List<Long> skillIds) {
        Job job = getJobById(jobId);
        if (job != null) {
            // Cập nhật thông tin job
            job.setJobName(jobDetails.getJobName());
            job.setJobDesc(jobDetails.getJobDesc());
            job.setSalary(jobDetails.getSalary());
            job.setCompany(jobDetails.getCompany());
            job.setStatus(jobDetails.getStatus());

            // Xóa các JobSkill cũ
            jobSkillRepository.deleteByJobId(jobId);
            job.getJobSkills().clear();

            // Thêm JobSkill mới
            for (Long skillId : skillIds) {
                Skill skill = skillRepository.findById(skillId).orElse(null);
                if (skill != null) {
                    JobSkill jobSkill = new JobSkill();
                    JobSkillId jobSkillId = new JobSkillId(jobId, skillId);
                    jobSkill.setId(jobSkillId);
                    jobSkill.setJob(job);
                    jobSkill.setSkill(skill);
                    jobSkillRepository.save(jobSkill);
                    job.getJobSkills().add(jobSkill);
                }
            }

            return jobRepository.save(job);
        }
        return null;
    }




}
