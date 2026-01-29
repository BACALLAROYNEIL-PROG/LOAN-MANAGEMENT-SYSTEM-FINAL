package com.cooperative.loan_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private int term;
    private double interest;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
