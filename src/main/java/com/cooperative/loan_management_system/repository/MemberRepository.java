package com.cooperative.loan_management_system.repository;

import com.cooperative.loan_management_system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // IMPORTANT: Must import this

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * UPDATED: Returns a List instead of a single Member.
     * This prevents the "NonUniqueResultException" crash if 
     * two members accidentally have the same name.
     */
    List<Member> findByFullName(String fullName);
    
}