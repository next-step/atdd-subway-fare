package atdd.user.docs;

import atdd.BaseDocumentationTest;
import atdd.user.application.UserService;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginDocumentationTest(MockMvc mockMvc,
                                  ObjectMapper objectMapper,
                                  JwtTokenProvider jwtTokenProvider) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @MockBean
    UserService userService;

    @Test
    void 문서화_로그인_요청하기() throws Exception {
        //given
        User user = new User(1L, EMAIL, NAME, PASSWORD);
        LoginRequestView requestView = new LoginRequestView(EMAIL, PASSWORD, jwtTokenProvider);
        String inputJson = objectMapper.writeValueAsString(requestView);
        given(userService.findByEmail(EMAIL)).willReturn(user);

        //when, then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("login",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("profile")
                                        .description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        requestFields(
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the visitor who wants to be a member"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("The password of the visitor who wants to be a member"),
                                fieldWithPath("accessToken")
                                        .type(JsonFieldType.STRING)
                                        .description("The client can see this if the request is valid"),
                                fieldWithPath("tokenType")
                                        .type(JsonFieldType.STRING)
                                        .description("The client can see this if the request is valid")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("accessToken")
                                        .type(JsonFieldType.STRING)
                                        .description("The accessToken provided by the server"),
                                fieldWithPath("tokenType")
                                        .type(JsonFieldType.STRING)
                                        .description("It should be Bearer type in this application"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }
}
