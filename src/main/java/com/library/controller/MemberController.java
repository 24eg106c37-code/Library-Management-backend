package com.library.controller;

import com.library.dto.MemberResponse;
import com.library.service.MemberService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService members;
    public MemberController(MemberService members) { this.members = members; }
    @GetMapping public List<MemberResponse> all() { return members.all(); }
    @GetMapping("/{id}") public MemberResponse get(@PathVariable @NonNull Long id) { return members.get(id); }
}