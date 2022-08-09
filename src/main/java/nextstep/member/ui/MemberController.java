package nextstep.member.ui;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.MemberUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public MemberResponse findMemberOfMine(@AuthenticationPrincipal User user) {
        return memberService.findMember(user.getUsername());
    }

    @PutMapping("/members/me")
    public void updateMemberOfMine(@AuthenticationPrincipal User user, @RequestBody @Valid MemberUpdateRequest param) {
        memberService.updateMember(user.getUsername(), param);
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal User user) {
        memberService.deleteMember(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}

