package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Company;
import vn.edu.iuh.fit.backEnd.repositories.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    // Placeholder: Implement email sending logic if needed
    public static void sendEmail(String email, String subject, String text) {
        // Email logic here (hiện đang để trống)
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElse(null); // Hoặc throw new NotFoundException(id)
    }

    public Company getCompanyByName(String name) {
        return companyRepository.findByCompName(name);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = getCompanyById(id);
        if (company == null) {
            throw new RuntimeException("Company not found with id " + id);
        }

        company.setCompName(companyDetails.getCompName());
        company.setAbout(companyDetails.getAbout());
        company.setEmail(companyDetails.getEmail());
        company.setPhone(companyDetails.getPhone());
        company.setWebUrl(companyDetails.getWebUrl());

        if (company.getAddress() != null && companyDetails.getAddress() != null) {
            company.getAddress().setAddId(companyDetails.getAddress().getAddId());
        } else {
            company.setAddress(companyDetails.getAddress());
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        if (company != null) {
            companyRepository.delete(company);
        } else {
            throw new RuntimeException("Company not found to delete with id " + id);
        }
    }

    // For login/registration features
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
