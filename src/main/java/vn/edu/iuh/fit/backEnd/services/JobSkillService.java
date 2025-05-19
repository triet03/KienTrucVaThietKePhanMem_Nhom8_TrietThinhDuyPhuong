package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.repositories.JobSkillRepository;
@Service
public class JobSkillService {
    @Autowired
    private JobSkillRepository jobSkillRepository;
    // Business logic methods
}

