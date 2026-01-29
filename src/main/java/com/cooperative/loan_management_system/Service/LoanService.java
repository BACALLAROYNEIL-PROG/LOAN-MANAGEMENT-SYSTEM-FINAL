package com.cooperative.loan_management_system.Service;

import com.cooperative.loan_management_system.entity.Loan;
import com.cooperative.loan_management_system.entity.LoanStatus;
import com.cooperative.loan_management_system.entity.Member; // Added Import
import com.cooperative.loan_management_system.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan saveLoan(Loan loan) {
        if (loan.getStatus() == null) {
            loan.setStatus(LoanStatus.PENDING);
        }
        return loanRepository.save(loan);
    }

    public Loan getById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    // --- WORKFLOW METHODS ---

    @Transactional
    public void approveLoan(Long id) {
        Loan loan = getById(id); 
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);
    }

    @Transactional
    public void rejectLoan(Long id) {
        Loan loan = getById(id);
        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
    }

    /**
     * Deletes a loan and forces a database sync
     */
    @Transactional
    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
        loanRepository.flush(); 
    }

    /**
     * NEW METHOD: Member Profile Support
     * Fetches all loans belonging to a specific member.
     */
    public List<Loan> getLoansByMember(Member member) {
        return loanRepository.findByMember(member);
    }
}