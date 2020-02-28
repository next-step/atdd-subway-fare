package atdd.member.docs;

import atdd.AbstractDocumentationTest;
import atdd.member.application.MemberService;
import atdd.member.domain.Member;
import atdd.member.web.MemberController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static atdd.TestConstant.*;
import static atdd.member.web.MemberController.MEMBER_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberDocumentationTest extends AbstractDocumentationTest {
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";

    @MockBean
    private MemberService memberService;

    @Test
    void create() throws Exception {
        Member member = new Member(1L, TEST_MEMBER_EMAIL, TEST_MEMBER_PASSWORD, TEST_MEMBER_NAME);

        String inputJson = "{\"email\":\"" + member.getEmail() + "\"," +
                "\"password\":\"" + member.getPassword() + "\"," +
                "\"name\":\"" + member.getName() + "\"}";

        given(memberService.save(any())).willReturn(member);

        this.mockMvc.perform(post(MEMBER_URL)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document(MEMBER_URL + "/create",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                                )
                        ))
                .andDo(print());
    }

    @Test
    void me() throws Exception {
        given(memberService.findMemberByEmail(anyString())).willReturn(new Member(1L, TEST_MEMBER_EMAIL, TEST_MEMBER_PASSWORD, TEST_MEMBER_NAME));

        this.mockMvc.perform(get(MEMBER_URL + "/me")
                .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_MEMBER_EMAIL))
                .andDo(
                        document(MEMBER_URL + "/me",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestHeaders(
                                        headerWithName("Authorization").description(
                                                "Bearer auth credentials")),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                                )
                        ))
                .andDo(print());
    }
}