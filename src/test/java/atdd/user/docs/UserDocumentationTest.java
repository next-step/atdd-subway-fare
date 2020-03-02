package atdd.user.docs;

import atdd.BaseDocumentationTest;
import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import atdd.user.domain.UserRepository;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserDocumentationTest(MockMvc mockMvc,
                                 ObjectMapper objectMapper,
                                 JwtTokenProvider jwtTokenProvider) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @Test
    void 문서화_회원_가입하기() throws Exception {
        //given
        User user = new User(1L, EMAIL, NAME, PASSWORD);
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        String inputJson = objectMapper.writeValueAsString(requestView);
        given(userService.createUser(any(CreateUserRequestView.class)))
                .willReturn(UserResponseView.of(user));

        //when, then
        mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("users-create",
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
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the visitor who wants to be a member"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("The password of the visitor who wants to be a member")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id that the server gives to user "),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the user"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("The password of the user"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    @Test
    void 문서화_회원_탈퇴하기() throws Exception {
        //given
        User user = new User(1L, EMAIL, NAME, PASSWORD);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        mockMvc.perform(delete("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users-delete",
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
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NULL)
                                        .description("The id that the server gives to user "),
                                fieldWithPath("email")
                                        .type(JsonFieldType.NULL)
                                        .description("The email address of the user"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.NULL)
                                        .description("The name of the user"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.NULL)
                                        .description("The password of the user"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    @Test
    void 문서화_회원정보_불러오기() throws Exception {
        //given
        User user = new User(1L, EMAIL, NAME, PASSWORD);
        given(userService.findByEmail(EMAIL)).willReturn(user);
        String token = jwtTokenProvider.createToken(EMAIL);

        //when, then
        mockMvc.perform(get("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users-me",
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
                                        .description("Its contentType is MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.AUTHORIZATION)
                                        .description("The client should send the token received from the server")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NULL)
                                        .description("It should be null"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the user"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("The password of the user"),
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
