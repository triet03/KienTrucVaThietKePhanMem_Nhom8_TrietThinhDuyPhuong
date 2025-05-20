package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.Skill;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	@Query("SELECT c FROM Candidate c JOIN CandidateSkill cs ON c.canId = cs.candidate.canId WHERE cs.skill = :skill")
	List<Candidate> findCandidatesBySkill(Skill skill);

	Optional<Candidate> findByEmail(String email);
}