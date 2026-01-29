package com.cooperative.loan_management_system.Controller;

import com.cooperative.loan_management_system.Service.MemberService;
import com.cooperative.loan_management_system.Service.LoanService; // Added this
import com.cooperative.loan_management_system.entity.Loan;
import com.cooperative.loan_management_system.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Added this

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private LoanService loanService; // Added this injection

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "member-list";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    @PostMapping("/save")
    public String saveMember(@ModelAttribute Member member) {
        memberService.saveMember(member);
        return "redirect:/members";
    }

    @GetMapping("/edit/{id}")
    public String editMember(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        return "member-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }

    @GetMapping("/profile/{id}")
    public String viewMemberProfile(@PathVariable("id") Long id, Model model) {
        // 1. Get the member details
        Member member = memberService.getMemberById(id);
        
        // 2. Get only the loans belonging to this member
        List<Loan> memberLoans = loanService.getLoansByMember(member);
        
        // 3. Calculate total balance for approved loans
        double totalOutstanding = memberLoans.stream()
            .filter(l -> l.getStatus() != null && l.getStatus().toString().equals("APPROVED"))
            .mapToDouble(Loan::getAmount)
            .sum();

        model.addAttribute("member", member);
        model.addAttribute("loans", memberLoans);
        model.addAttribute("totalBalance", totalOutstanding);
        
        return "member-profile";
    }
}