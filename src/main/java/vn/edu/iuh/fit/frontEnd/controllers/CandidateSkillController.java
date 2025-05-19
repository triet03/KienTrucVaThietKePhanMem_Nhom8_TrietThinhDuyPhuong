package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.CandidateSkill;
import vn.edu.iuh.fit.backEnd.models.CandidateSkillId;
import vn.edu.iuh.fit.backEnd.services.CandidateService;
import vn.edu.iuh.fit.backEnd.services.CandidateSkillService;
import vn.edu.iuh.fit.backEnd.services.SkillService;

import java.util.List;

@Controller
@RequestMapping("/candidate-skills")
public class CandidateSkillController {
    @Autowired
    private CandidateSkillService candidateSkillService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private SkillService skillService;

    @GetMapping
    public String getAllCandidateSkills(Model model) {
        List<CandidateSkill> candidateSkills = candidateSkillService.getAllCandidateSkills();
        model.addAttribute("candidateSkills", candidateSkills);
        return "candidate-skill-list";
    }

    @GetMapping("/{id}")
    public String getCandidateSkillById(@PathVariable CandidateSkillId id, Model model) {
        CandidateSkill candidateSkill = candidateSkillService.getCandidateSkillById(id);
        model.addAttribute("candidateSkill", candidateSkill);
        return "candidate-skill-detail";
    }

    @GetMapping("/add")
    public String showAddCandidateSkillForm(Model model) {
        model.addAttribute("candidates", candidateService.getAllCandidates());
        model.addAttribute("skills", skillService.getAllSkills());
        return "add-candidate-skill";
    }

    @PostMapping
    public String createCandidateSkill(@RequestParam Long candidateId, @RequestParam Long skillId, @RequestParam Byte skillLevel, @RequestParam String moreInfos) {
        candidateSkillService.addSkillToCandidate(candidateId, skillId, skillLevel, moreInfos);
        return "redirect:/candidate-skills";
    }

    @DeleteMapping("/{id}")
    public String deleteCandidateSkill(@PathVariable CandidateSkillId id) {
        candidateSkillService.deleteCandidateSkill(id);
        return "redirect:/candidate-skills";
    }
}