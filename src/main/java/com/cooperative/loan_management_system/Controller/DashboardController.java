package com.cooperative.loan_management_system.Controller;

import com.cooperative.loan_management_system.Service.LoanService;
import com.cooperative.loan_management_system.Service.MemberService; // Added for member count
import com.cooperative.loan_management_system.entity.LoanStatus;
import com.cooperative.loan_management_system.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private MemberService memberService; // Injected MemberService

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        // 1. Get Loan counts safely
        long totalLoans = loanRepository.count(); 
        long pendingLoans = loanRepository.countByStatus(LoanStatus.PENDING);
        long approvedLoans = loanRepository.countByStatus(LoanStatus.APPROVED);
        
        // 2. NEW: Get Total Members count
        long totalMembers = memberService.getAllMembers().size();
        
        // 3. Handle the Sum for approved loans
        Double totalReleased = loanRepository.sumApprovedLoans();
        if (totalReleased == null) {
            totalReleased = 0.0;
        }

        // 4. Add everything to the Model
        model.addAttribute("totalCount", totalLoans);
        model.addAttribute("pendingCount", pendingLoans);
        model.addAttribute("approvedCount", approvedLoans);
        model.addAttribute("totalReleased", totalReleased);
        model.addAttribute("totalMembers", totalMembers); // Added for HTML
        
        // 5. Send the list for the Recent Activity table
        model.addAttribute("loans", loanService.getAllLoans());
        
        return "dashboard";
    }
}