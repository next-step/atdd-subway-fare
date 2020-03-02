package atdd.member.web;

import atdd.member.application.MemberService;
import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import atdd.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

    public static final String MEMBER_URL = "/members";

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponseView> createUser(@RequestBody CreateMemberRequestView view) {
        final Member savedMember = memberService.save(view.toMember());
        return ResponseEntity.created(URI.create(MEMBER_URL +"/" + savedMember.getId()))
                .body(new MemberResponseView(savedMember));
    }

    @GetMapping("/me")
    public ResponseEntity retrieveUser(@LoginUser Member member) {
        Member persistMember = memberService.findMemberByEmail(member.getEmail());
        return ResponseEntity.ok().body(new MemberResponseView(persistMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.noContent().build();
    }

}
