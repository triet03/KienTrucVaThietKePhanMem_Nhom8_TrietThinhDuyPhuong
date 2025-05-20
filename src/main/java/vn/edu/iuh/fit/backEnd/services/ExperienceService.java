package vn.edu.iuh.fit.backEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.repositories.ExperienceRepository;

@Service
public class ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;
    // Business logic methods
}

