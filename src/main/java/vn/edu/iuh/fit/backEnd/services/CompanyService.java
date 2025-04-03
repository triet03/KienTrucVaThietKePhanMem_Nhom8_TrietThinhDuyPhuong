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

	public static void sendEmail(String email, String subject, String text) {
	}

	public List<Company> getAllCompanies() {
	        return companyRepository.findAll();
	    }

	    public Company getCompanyById(Long id) {
	        Optional<Company> company = companyRepository.findById(id);
	        if (company.isPresent()) {
	            return company.get();
	        } else {

	            return null;  // Hoặc bạn có thể tạo một ngoại lệ tùy chỉnh nếu cần
	        }
	    }


	    public Company createCompany(Company company) {
	        return companyRepository.save(company);
	    }

	    public Company updateCompany(Long id, Company companyDetails) {
	        Company company = getCompanyById(id);
	        company.setCompName(companyDetails.getCompName());
	        company.setAbout(companyDetails.getAbout());
	        company.setEmail(companyDetails.getEmail());
	        company.setPhone(companyDetails.getPhone());
	        company.setWebUrl(companyDetails.getWebUrl());
	        company.setAddress(companyDetails.getAddress());
	        return companyRepository.save(company);
	    }

	    public void deleteCompany(Long id) {
	        Company company = getCompanyById(id);
	        companyRepository.delete(company);
	    }
}

