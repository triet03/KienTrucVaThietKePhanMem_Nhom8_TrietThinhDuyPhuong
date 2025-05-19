package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private JobSkillRepository jobSkillRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.orElse(null);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

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

    public Job updateJob(Long id, Job jobDetails) {
        Job job = getJobById(id);
        job.setJobName(jobDetails.getJobName());
        job.setJobDesc(jobDetails.getJobDesc());
        job.setCompany(jobDetails.getCompany());
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }
}