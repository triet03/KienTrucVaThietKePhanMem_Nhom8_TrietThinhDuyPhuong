package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.models.SavedJob;
import vn.edu.iuh.fit.backEnd.repositories.SavedJobRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SavedJobService {
    @Autowired
    private SavedJobRepository savedJobRepository;

    public void saveJob(Candidate candidate, Job job) {
        if (!savedJobRepository.existsByCandidateAndJob(candidate, job)) {
            SavedJob saved = new SavedJob();
            saved.setCandidate(candidate);
            saved.setJob(job);
            savedJobRepository.save(saved);
        }
    }

    public List<SavedJob> getSavedJobs(Candidate candidate) {
        return savedJobRepository.findByCandidate(candidate);
    }
    public void deleteByIdAndCandidate(Long id, Candidate candidate) {
        Optional<SavedJob> optional = savedJobRepository.findById(id);
        optional.ifPresent(savedJob -> {
            if (savedJob.getCandidate().getCanId().equals(candidate.getCanId())) {
                savedJobRepository.delete(savedJob);
            }
        });
    }
}

