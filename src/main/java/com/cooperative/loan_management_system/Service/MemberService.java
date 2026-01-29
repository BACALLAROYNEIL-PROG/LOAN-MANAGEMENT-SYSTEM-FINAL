package com.cooperative.loan_management_system.Service;

import com.cooperative.loan_management_system.entity.Member;
import com.cooperative.loan_management_system.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    /**
     * FIXED METHOD
     * We use a List from the repository to prevent crashes if 
     * duplicate names accidentally exist in the database.
     */
    public Member getMemberByName(String name) {
        List<Member> members = memberRepository.findByFullName(name);
        if (members.isEmpty()) {
            return null;
        }
        // If duplicates exist, we safely return the first one found 
        // instead of throwing a 500 error.
        return members.get(0);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}