package atdd.member.docs;

import atdd.AbstractDocumentationTest;
import atdd.member.application.MemberService;
import atdd.member.web.MemberController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Map;

import static atdd.TestConstant.*;
import static atdd.TestUtils.jsonOf;
import static atdd.member.web.MemberController.MEMBER_URL;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private MemberService memberService;

    @Test
    void beAbleToJoin() throws Exception {
        Map<String, Object> inputJson = Map.ofEntries(
                Map.entry("email", TEST_MEMBER_EMAIL),
                Map.entry("name", TEST_MEMBER_NAME),
                Map.entry("password", TEST_MEMBER_PASSWORD)
        );

        given(memberService.save(any())).willReturn(TEST_MEMBER);

        this.mockMvc.perform(post(MEMBER_URL)
                .content(jsonOf(inputJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document(MEMBER_URL + "/create",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The member's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The member's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The member's name")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The member's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The member's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The member's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The member's name")
                                )
                        ))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void beAbleToWithdrawal() throws Exception {
        doNothing().when(memberService).deleteById(MEMBER_ID);

        mockMvc.perform(delete(MEMBER_URL + "/{id}", MEMBER_ID)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(MEMBER_URL + "/delete",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer auth credentials")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("The member's id")
                                )))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void beAbleToFindMe() throws Exception {
        given(memberService.findMemberByEmail(anyString())).willReturn(TEST_MEMBER);

        this.mockMvc.perform(get(MEMBER_URL + "/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_MEMBER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document(MEMBER_URL + "/me",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer auth credentials")),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The member's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The member's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The member's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The member's name")
                                )
                        ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_MEMBER_EMAIL));
    }
}