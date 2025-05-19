package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.Experience;
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {}
