package vn.edu.iuh.fit.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backEnd.models.Company;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByVerificationToken(String token);

    // ✅ Thêm truy vấn để lấy danh sách city duy nhất
    @Query("SELECT DISTINCT a.city FROM Company c JOIN c.address a WHERE a.city IS NOT NULL")
    List<String> findDistinctCities();
}
