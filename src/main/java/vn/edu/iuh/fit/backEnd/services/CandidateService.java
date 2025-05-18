package vn.edu.iuh.fit.backEnd.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Candidate;
import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
import vn.edu.iuh.fit.backEnd.models.VerificationCode;
import vn.edu.iuh.fit.backEnd.repositories.CandidateRepository;
import vn.edu.iuh.fit.backEnd.repositories.CandidateSkillRepository;
import vn.edu.iuh.fit.backEnd.repositories.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        return optionalCandidate.orElse(null);
    }

    @Cacheable(value = "candidates", key = "#email")
    public Candidate findByEmail(String email) {
        logger.info("Calling findByEmail with email: {}", email);
        return candidateRepository.findByEmail(email).orElse(null);
    }

    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(Long id, Candidate candidateDetails) {
        Candidate candidate = getCandidateById(id);
        candidate.setFullName(candidateDetails.getFullName());
        candidate.setEmail(candidateDetails.getEmail());
        candidate.setPhone(candidateDetails.getPhone());
        candidate.setDob(candidateDetails.getDob());
        candidate.setAddress(candidateDetails.getAddress());
        candidate.setPassword(candidateDetails.getPassword());
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = getCandidateById(id);
        candidateRepository.delete(candidate);
    }

    public List<CandidateSkill> getCandidateSkills(Long canId) {
        return candidateSkillRepository.findByCandidateCanId(canId);
    }

    public void sendAndSaveVerificationCode(String email) {
        String code = emailService.generateVerificationCode();
        emailService.sendVerificationCode(email, code);

        // Lưu mã xác thực với thời gian hết hạn 10 phút
        VerificationCode verificationCode = new VerificationCode(
                email,
                code,
                LocalDateTime.now().plusMinutes(10)
        );
        verificationCodeRepository.save(verificationCode);
    }

    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmailAndCode(email, code);
        if (optionalCode.isPresent()) {
            VerificationCode verificationCode = optionalCode.get();
            if (!verificationCode.isExpired()) {
                verificationCodeRepository.delete(verificationCode); // Xóa mã sau khi xác thực thành công
                return true;
            }
        }
        return false;
    }

    public void deleteVerificationCodeByEmail(String email) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmail(email);
        optionalCode.ifPresent(verificationCodeRepository::delete);
    }
}