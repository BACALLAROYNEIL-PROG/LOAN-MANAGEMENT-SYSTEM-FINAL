package com.cooperative.loan_management_system.Controller;

import com.cooperative.loan_management_system.Service.LoanService;
import com.cooperative.loan_management_system.Service.MemberService;
import com.cooperative.loan_management_system.entity.Loan;
import com.cooperative.loan_management_system.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List; // Added

@Controller
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanService.getAllLoans());
        return "loan-list";
    }

    @GetMapping("/new")
    public String newLoan(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("members", memberService.getAllMembers());
        return "loan-form";
    }

    /**
     * FIXED SAVE METHOD
     * We now handle "NonUniqueResultException" by checking for a list of members.
     */
    @PostMapping("/save")
    public String saveLoan(@ModelAttribute Loan loan,
                           @RequestParam("memberName") String memberName) {
        
        // 1. Get all members with this name (to handle duplicates safely)
        List<Member> existingMembers = memberService.getAllMembers().stream()
                .filter(m -> m.getFullName().equalsIgnoreCase(memberName.trim()))
                .toList();
        
        Member member;

        // 2. Decide whether to use existing or create new
        if (!existingMembers.isEmpty()) {
            // Duplicate found? No problem. Pick the first one.
            member = existingMembers.get(0);
        } else {
            // New name? Create the member.
            member = new Member();
            member.setFullName(memberName);
            member = memberService.saveMember(member); 
        }
        
        // 3. Link and Save
        loan.setMember(member);
        loanService.saveLoan(loan);
        
        return "redirect:/loans";
    }

    @PostMapping("/approve/{id}")
    public String approveLoan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            loanService.approveLoan(id);
            ra.addFlashAttribute("message", "Loan approved successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error approving loan: " + e.getMessage());
        }
        return "redirect:/loans";
    }

    @PostMapping("/reject/{id}")
    public String rejectLoan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            loanService.rejectLoan(id);
            ra.addFlashAttribute("message", "Loan has been rejected.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error rejecting loan: " + e.getMessage());
        }
        return "redirect:/loans";
    }

    @PostMapping("/delete/{id}")
    public String deleteLoan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            loanService.deleteLoan(id); 
            ra.addFlashAttribute("message", "Loan deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/loans";
    }
}