package com.cooperative.loan_management_system.repository;

import com.cooperative.loan_management_system.entity.Loan;
import com.cooperative.loan_management_system.entity.LoanStatus;
import com.cooperative.loan_management_system.entity.Member; // Added this import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List; // Added this import

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Count loans based on the Enum status
    long countByStatus(LoanStatus status);
    
    // Calculate the sum of all approved loan amounts
    @Query("SELECT SUM(l.amount) FROM Loan l WHERE l.status = com.cooperative.loan_management_system.entity.LoanStatus.APPROVED")
    Double sumApprovedLoans();

    // NEW METHOD: Required for the Member Profile View
    List<Loan> findByMember(Member member);
}