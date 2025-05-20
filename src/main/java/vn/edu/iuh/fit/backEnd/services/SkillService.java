package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.*;
import vn.edu.iuh.fit.backEnd.repositories.CandidateSkillRepository;
import vn.edu.iuh.fit.backEnd.repositories.SkillRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        return skill.orElse(null);  // Trả về đối tượng Skill nếu tìm thấy, nếu không trả về null
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, Skill skillDetails) {
        Skill skill = getSkillById(id);
        if (skill != null) {
            skill.setSkillName(skillDetails.getSkillName());
            skill.setSkillDesc(skillDetails.getSkillDesc());
            skill.setSkillType(skillDetails.getSkillType());
            return skillRepository.save(skill);
        }
        return null;
    }

    public void deleteSkill(Long id) {
        Skill skill = getSkillById(id);
        if (skill != null) {
            skillRepository.delete(skill);
        }
    }

    public List<Skill> recommendSkillsForCandidate(Long candidateId) {
        List<CandidateSkill> candidateSkills = candidateSkillRepository.findByCandidateCanId(candidateId);
        List<Skill> allSkills = skillRepository.findAll();
        List<Skill> recommendedSkills = new ArrayList<>();

        for (Skill skill : allSkills) {
            boolean hasSkill = false;
            for (CandidateSkill candidateSkill : candidateSkills) {
                if (candidateSkill.getSkill().equals(skill)) {
                    hasSkill = true;
                    break;
                }
            }
            if (!hasSkill) {
                recommendedSkills.add(skill);
            }
        }
        return recommendedSkills;
    }
}