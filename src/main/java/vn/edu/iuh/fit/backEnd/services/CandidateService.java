package vn.edu.iuh.fit.backEnd.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.repositories.CandidateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    /**
     * Lấy tất cả các ứng viên
     * @return danh sách ứng viên
     */
    @Transactional(readOnly = true)
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    /**
     * Lấy ứng viên theo ID
     * @param id ID của ứng viên
     * @return ứng viên hoặc null nếu không tìm thấy
     * @throws IllegalArgumentException nếu id là null
     */
    @Transactional(readOnly = true)
    public Candidate getCandidateById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        return optionalCandidate.orElse(null);
    }

    /**
     * Tạo mới một ứng viên
     * @param candidate đối tượng ứng viên cần tạo
     * @return ứng viên đã được lưu
     */
    @Transactional
    public Candidate createCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        return candidateRepository.save(candidate);
    }

    /**
     * Cập nhật thông tin ứng viên
     * @param id ID của ứng viên cần cập nhật
     * @param candidateDetails thông tin mới của ứng viên
     * @return ứng viên đã được cập nhật
     * @throws EntityNotFoundException nếu không tìm thấy ứng viên
     */
    @Transactional
    public Candidate updateCandidate(Long id, Candidate candidateDetails) {
        if (id == null || candidateDetails == null) {
            throw new IllegalArgumentException("ID or candidate details cannot be null");
        }
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + id));

        // Cập nhật các trường từ candidateDetails
        candidate.setFullName(candidateDetails.getFullName());
        candidate.setEmail(candidateDetails.getEmail());
        candidate.setPhone(candidateDetails.getPhone());
        candidate.setDob(candidateDetails.getDob());
        candidate.setAddress(candidateDetails.getAddress());
        candidate.setSummary(candidateDetails.getSummary()); // Đảm bảo bao gồm summary

        return candidateRepository.save(candidate);
    }

    /**
     * Xóa ứng viên theo ID
     * @param id ID của ứng viên cần xóa
     * @throws EntityNotFoundException nếu không tìm thấy ứng viên
     */
    @Transactional
    public void deleteCandidate(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + id));
        candidateRepository.delete(candidate);
    }

    /**
     * Lưu ứng viên (dùng cho cả tạo mới và cập nhật)
     * @param candidate đối tượng ứng viên cần lưu
     * @return ứng viên đã được lưu
     */
    @Transactional
    public Candidate saveCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        return candidateRepository.save(candidate);
    }

    /**
     * Kiểm tra xem email đã tồn tại chưa
     * @param email email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu không
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        return candidateRepository.findByEmail(email) != null;
    }

    /**
     * Đăng nhập ứng viên
     * @param email email của ứng viên
     * @param password mật khẩu
     * @return ứng viên nếu đăng nhập thành công, null nếu thất bại
     */
    @Transactional(readOnly = true)
    public Candidate login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        Candidate candidate = candidateRepository.findByEmail(email);
        if (candidate != null && candidate.getPassword().equals(password)) {
            return candidate;
        }
        return null;
    }

    /**
     * Tìm ứng viên theo email
     * @param email email của ứng viên
     * @return ứng viên hoặc null nếu không tìm thấy
     */
    @Transactional(readOnly = true)
    public Candidate findByEmail(String email) {
        if (email == null) {
            return null;
        }
        return candidateRepository.findByEmail(email);
    }
}