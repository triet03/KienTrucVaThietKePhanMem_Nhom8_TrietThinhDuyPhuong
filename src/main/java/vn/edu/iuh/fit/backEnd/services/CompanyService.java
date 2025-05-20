package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
	@Autowired
	private CompanyRepository companyRepository;

	// Gửi email (nếu cần mở rộng)
	public static void sendEmail(String email, String subject, String text) {
		// Email logic here (hiện đang để trống)
	}

	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}

	public Company getCompanyById(Long id) {
		return companyRepository.findById(id).orElse(null);
	}

	public Company createCompany(Company company) {
		return companyRepository.save(company);
	}

	public Company updateCompany(Long id, Company companyDetails) {
		Company company = getCompanyById(id);
		if (company != null) {
			company.setCompName(companyDetails.getCompName());
			company.setAbout(companyDetails.getAbout());
			company.setEmail(companyDetails.getEmail());
			company.setPhone(companyDetails.getPhone());
			company.setWebUrl(companyDetails.getWebUrl());
			company.setAddress(companyDetails.getAddress());
			return companyRepository.save(company);
		}
		return null;
	}

	public void deleteCompany(Long id) {
		Company company = getCompanyById(id);
		if (company != null) {
			companyRepository.delete(company);
		}
	}

	// 🔥 Thêm cho đăng ký / đăng nhập
	public Company findByEmail(String email) {
		return companyRepository.findByEmail(email).orElse(null);
	}


	public boolean existsByEmail(String email) {
		return companyRepository.findByEmail(email).isPresent();
	}

	public void save(Company company) {
		companyRepository.save(company);
	}
}
