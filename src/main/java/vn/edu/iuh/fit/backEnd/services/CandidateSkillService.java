package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
import vn.edu.iuh.fit.backEnd.models.CandidateSkillId;
import vn.edu.iuh.fit.backEnd.models.Skill;
import vn.edu.iuh.fit.backEnd.repositories.CandidateRepository;
import vn.edu.iuh.fit.backEnd.repositories.CandidateSkillRepository;
import vn.edu.iuh.fit.backEnd.repositories.SkillRepository;

import java.util.List;

@Service
public class CandidateSkillService {
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private SkillRepository skillRepository;

    public List<CandidateSkill> getAllCandidateSkills() {
        return candidateSkillRepository.findAll();
    }

    public CandidateSkill getCandidateSkillById(CandidateSkillId id) {
        return candidateSkillRepository.findById(id).orElse(null);
    }

    public CandidateSkill saveCandidateSkill(CandidateSkill candidateSkill) {
        return candidateSkillRepository.save(candidateSkill);
    }

    public void deleteCandidateSkill(CandidateSkillId id) {
        candidateSkillRepository.deleteById(id);
    }

    public CandidateSkill addSkillToCandidate(Long candidateId, Long skillId, Byte skillLevel, String moreInfos) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        Skill skill = skillRepository.findById(skillId).orElse(null);

        if (candidate != null && skill != null) {
            CandidateSkillId id = new CandidateSkillId();
            id.setCanId(candidateId);
            id.setSkillId(skillId);

            CandidateSkill candidateSkill = new CandidateSkill();
            candidateSkill.setId(id);
            candidateSkill.setCandidate(candidate);
            candidateSkill.setSkill(skill);
            candidateSkill.setSkillLevel(skillLevel);
            candidateSkill.setMoreInfos(moreInfos);

            return candidateSkillRepository.save(candidateSkill);
        }
        return null;
    }
}