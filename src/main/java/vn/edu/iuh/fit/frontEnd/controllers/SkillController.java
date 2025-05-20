package vn.edu.iuh.fit.frontEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.backEnd.models.Skill;
import vn.edu.iuh.fit.backEnd.services.CandidateService;
import vn.edu.iuh.fit.backEnd.services.CandidateSkillService;
import vn.edu.iuh.fit.backEnd.services.SkillService;

import java.util.List;

@Controller
@RequestMapping("/skills")
public class SkillController {
    @Autowired
    private SkillService skillService;
    @Autowired
    private CandidateSkillService candidateSkillService;
    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public String getAllSkills(Model model) {
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);
        return "skill-list";
    }

    @GetMapping("/{id}")
    public String getSkillById(@PathVariable Long id, Model model) {
        Skill skill = skillService.getSkillById(id);
        model.addAttribute("skill", skill);
        return "skill-detail";
    }

    @GetMapping("/add")
    public String showAddSkillForm(Model model) {
        model.addAttribute("skill", new Skill());
        return "add-skill";
    }

    @PostMapping
    public String createSkill(@ModelAttribute Skill skill) {
        skillService.createSkill(skill);
        return "redirect:/skills";
    }

    @GetMapping("/assign")
    public String showAssignSkillForm(Model model) {
        model.addAttribute("candidates", candidateService.getAllCandidates());
        System.out.println(candidateService.getAllCandidates());
        model.addAttribute("skills", skillService.getAllSkills());
        System.out.println(skillService.getAllSkills());
        return "assign-skill";
    }

    @PostMapping("/assign")
    public String assignSkillToCandidate(@RequestParam Long candidateId, @RequestParam Long skillId, @RequestParam Byte skillLevel, @RequestParam String moreInfos) {
        candidateSkillService.addSkillToCandidate(candidateId, skillId, skillLevel, moreInfos);
        return "redirect:/skills";
    }
}