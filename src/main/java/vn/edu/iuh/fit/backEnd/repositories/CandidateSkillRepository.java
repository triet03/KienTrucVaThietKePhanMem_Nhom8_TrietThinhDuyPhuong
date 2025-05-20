package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
import vn.edu.iuh.fit.backEnd.models.CandidateSkillId;

import java.util.List;
@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, CandidateSkillId> {
	List<CandidateSkill> findByCandidateCanId(Long candidateId);
}