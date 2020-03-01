package atdd.login.docs;

import atdd.AbstractDocumentationTest;
import atdd.login.web.LoginController;
import atdd.member.application.MemberService;
import atdd.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Map;

import static atdd.TestConstant.*;
import static atdd.TestUtils.jsonOf;
import static atdd.login.web.LoginController.LOGIN_URL;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void beAbleToLogin() throws Exception {
        Map<String, Object> inputJson = Map.ofEntries(
                Map.entry("email", TEST_MEMBER_EMAIL),
                Map.entry("password", TEST_MEMBER_PASSWORD)
        );

        given(memberService.findMemberByEmail(TEST_MEMBER_EMAIL)).willReturn(TEST_MEMBER);
        given(jwtTokenProvider.createToken(TEST_MEMBER_EMAIL)).willReturn(TEST_MEMBER_TOKEN);

        this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonOf(inputJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document(LOGIN_URL,
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The member's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The member's password")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("tokenType").type(JsonFieldType.STRING).description("Type auth credentials"),
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Bearer auth credentials")
                                )
                        ))
                .andDo(print())
                .andExpect(status().isOk());
    }

}