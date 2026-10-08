package com.library.service;

import com.library.dto.MemberResponse;
import com.library.exception.NotFoundException;
import com.library.repository.MemberRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository members;
    public MemberService(MemberRepository members) { this.members = members; }
    public List<MemberResponse> all() { return members.findAll().stream().map(MemberResponse::from).toList(); }
    public MemberResponse get(@NonNull Long id) { return MemberResponse.from(members.findById(id).orElseThrow(() -> new NotFoundException("Member not found."))); }
}