package nextstep.api.documentation.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.member.MemberSteps.내_정보_조회_요청;
import static nextstep.api.acceptance.member.MemberSteps.회원_삭제_요청;
import static nextstep.api.acceptance.member.MemberSteps.회원_정보_수정_요청;
import static nextstep.api.acceptance.member.MemberSteps.회원_정보_조회_요청;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import nextstep.api.acceptance.member.MemberSteps;
import nextstep.api.auth.aop.principal.UserPrincipal;
import nextstep.api.auth.support.JwtTokenProvider;
import nextstep.api.documentation.Documentation;
import nextstep.api.member.application.MemberService;
import nextstep.api.member.application.dto.MemberResponse;

class MemberDocumentation extends Documentation {
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MemberService memberService;

    private final String email = "user@email.com";
    private final String password = "password";
    private final Integer age = 20;
    private final MemberResponse response = new MemberResponse(1L, email, age);

    @Test
    void createMember() {
        when(memberService.createMember(any())).thenReturn(response);

        MemberSteps.회원_생성_요청(email, password, age, makeRequestSpec(
                document("member-create",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("age").description("나이")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 id"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("age").description("나이")
                        )
                )
        ));
    }

    @Test
    void updateMember() {
        doNothing().when(memberService).updateMember(anyLong(), any());

        회원_정보_수정_요청(1L, email, password, age, makeRequestSpec(
                document("member-update",
                        pathParameters(
                                parameterWithName("id").description("회원 id")
                        ),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("age").description("나이")
                        )
                )
        ));
    }

    @Test
    void deleteMember() {
        doNothing().when(memberService).deleteMember(anyLong());

        회원_삭제_요청(1L, makeRequestSpec(
                document("member-delete",
                        pathParameters(
                                parameterWithName("id").description("회원 id")
                        )
                )
        ));
    }

    @Test
    void findMember() {
        when(memberService.findMember(1L)).thenReturn(response);

        회원_정보_조회_요청(1L, makeRequestSpec(
                document("member-find",
                        pathParameters(
                                parameterWithName("id").description("회원 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 id"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("age").description("나이")
                        )
                )
        ));
    }

    @Test
    void findMemberOfMine() {
        when(jwtTokenProvider.getUserPrincipal(anyString())).thenReturn(new UserPrincipal("", ""));
        when(memberService.findMember(anyString())).thenReturn(response);

        내_정보_조회_요청("token", makeRequestSpec(
                document("member-find-me",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("bearer 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 id"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("age").description("나이")
                        )
                )
        ));
    }
}
