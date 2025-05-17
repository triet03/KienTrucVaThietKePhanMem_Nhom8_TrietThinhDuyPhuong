package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.JobSkill;
import vn.edu.iuh.fit.backEnd.repositories.CandidateRepository;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;
import vn.edu.iuh.fit.backEnd.repositories.JobSkillRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateRecommendationService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private JobSkillRepository jobSkillRepository;

    @Autowired
    private JobRepository jobRepository;


//    public List<Candidate> recommendCandidatesForJob(Long jobId) {
//        List<JobSkill> jobSkills = jobSkillRepository.findByJobId(jobId);
//        List<Candidate> recommendedCandidates = new ArrayList<>();
//
//        for (JobSkill jobSkill : jobSkills) {
//            List<Candidate> candidates = candidateRepository.findBySkills(jobSkill.getSkill());
//            for (Candidate candidate : candidates) {
//                if (!recommendedCandidates.contains(candidate)) {
//                    recommendedCandidates.add(candidate);
//                }
//            }
//        }
//        return recommendedCandidates;
//    }
//    public List<Candidate> recommendCandidatesForJob(Long jobId) {
//        List<JobSkill> jobSkills = jobSkillRepository.findByJobId(jobId);
//        List<Candidate> recommendedCandidates = new ArrayList<>();
//
//        for (JobSkill jobSkill : jobSkills) {
//            List<Candidate> candidates = candidateRepository.findCandidatesBySkill(jobSkill.getSkill());
//            for (Candidate candidate : candidates) {
//                if (!recommendedCandidates.contains(candidate)) {
//                    recommendedCandidates.add(candidate);
//                }
//            }
//        }
//        return recommendedCandidates;
//    }
    public List<Candidate> recommendCandidatesForJob(Long jobId) {
        List<JobSkill> jobSkills = jobSkillRepository.findByJobId(jobId);
        List<Candidate> recommendedCandidates = new ArrayList<>();

        for (JobSkill jobSkill : jobSkills) {
            List<Candidate> candidates = candidateRepository.findCandidatesBySkill(jobSkill.getSkill());
            for (Candidate candidate : candidates) {
                if (!recommendedCandidates.contains(candidate)) {
                    recommendedCandidates.add(candidate);
                }
            }
        }
        return recommendedCandidates;
    }

    public void sendEmailToCandidate(Long candidateId, Long jobId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        Job job = jobRepository.findById(jobId).orElse(null);

        if (candidate != null && job != null) {
            String subject = "Job Recommendation";
            String text = "Dear " + candidate.getFullName() + ",\n\n" +
                          "We have a job recommendation for you based on your skills.\n\n" +
                          "Company: " + job.getCompany().getCompName() + "\n" +
                          "Job Title: " + job.getJobName() + "\n" +
                          "Job Description: " + job.getJobDesc() + "\n" +
                          "Required Skills: " + jobSkillsToString(job.getJobId()) + "\n\n" +
                          "Best regards,\nYour Company";
            CompanyService.sendEmail(candidate.getEmail(), subject, text);
        }
    }

    private String jobSkillsToString(Long jobId) {
        List<JobSkill> jobSkills = jobSkillRepository.findByJobId(jobId);
        StringBuilder skills = new StringBuilder();
        for (JobSkill jobSkill : jobSkills) {
            skills.append(jobSkill.getSkill().getSkillName()).append(", ");
        }
        return skills.length() > 0 ? skills.substring(0, skills.length() - 2) : "";
    }
}