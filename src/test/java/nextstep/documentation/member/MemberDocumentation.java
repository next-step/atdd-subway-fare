package nextstep.documentation.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static nextstep.api.member.acceptance.MemberSteps.내_정보_조회_요청;
import static nextstep.api.member.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.api.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.api.member.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.api.member.acceptance.MemberSteps.회원_정보_조회_요청;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.member.application.MemberService;
import nextstep.api.member.application.dto.MemberResponse;
import nextstep.documentation.Documentation;

class MemberDocumentation extends Documentation {
    @MockBean
    private MemberService memberService;

    private final String email = "user@email.com";
    private final String password = "password";
    private final Integer age = 20;
    private final MemberResponse response = new MemberResponse(1L, email, age);

    @Test
    void createMember() {
        when(memberService.createMember(any())).thenReturn(new MemberResponse(1L,email, age));

        회원_생성_요청(email, password, age, makeRequestSpec("member-create"));
    }

    @Test
    void updateMember() {
        doNothing().when(memberService).updateMember(anyLong(), any());

        회원_정보_수정_요청(1L, email, password, age, makeRequestSpec("member-update"));
    }

    @Test
    void deleteMember() {
        doNothing().when(memberService).deleteMember(anyLong());

        회원_삭제_요청(1L, makeRequestSpec("member-delete"));
    }

    @Test
    void findMember() {
        when(memberService.findMember(anyLong())).thenReturn(response);

        회원_정보_조회_요청(1L, makeRequestSpec("member-find"));
    }

    @Test
    void findMemberOfMine() {
        when(memberService.findMember(anyString())).thenReturn(response);

        내_정보_조회_요청("token", makeRequestSpec("member-find-me"));
    }
}
