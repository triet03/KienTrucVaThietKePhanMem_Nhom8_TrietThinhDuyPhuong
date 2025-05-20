package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.repositories.CandidateSkillRepository;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobRecommendationService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

//    public List<Job> recommendJobsForCandidate(Long candidateId) {
//        List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateCanId(candidateId);
//        List<Job> recommendedJobs = new ArrayList<>();
//
//        for (CandidateSkill candidateSkill : candidateSkills) {
//            List<Job> jobs = jobRepository.findBySkills(candidateSkill.getSkill());
//            for (Job job : jobs) {
//                if (!recommendedJobs.contains(job)) {
//                    recommendedJobs.add(job);
//                }
//            }
//        }
//        return recommendedJobs;
//    }
    public List<Job> recommendJobsForCandidate(Long candidateId) {
        List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateCanId(candidateId);
        List<Job> recommendedJobs = new ArrayList<>();

        for (CandidateSkill candidateSkill : candidateSkills) {
            List<Job> jobs = jobRepository.findJobsBySkill(candidateSkill.getSkill());
            for (Job job : jobs) {
                if (!recommendedJobs.contains(job)) {
                    recommendedJobs.add(job);
                }
            }
        }
        return recommendedJobs;
    }
}