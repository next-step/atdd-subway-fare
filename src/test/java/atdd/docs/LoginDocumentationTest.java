package atdd.docs;

import atdd.Constant;
import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.Constant.USER_BASE_URI;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginDocumentationTest extends BaseDocumentationTest {
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserService userService;

    @Autowired
    public LoginDocumentationTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Test
    public void 로그인하면_토큰이_발급된다() throws Exception {
        LoginRequestView loginRequestView = new LoginRequestView(EMAIL, PASSWORD);
        String inputJson = objectMapper.writeValueAsString(loginRequestView);
        given(userService.findByEmail(EMAIL)).willReturn(new User(NAME, EMAIL, PASSWORD));

        mockMvc.perform(
                post(Constant.LOGIN_BASE_URI)
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("login",
                                links(halLinks(),
                                        linkWithRel("self")
                                                .description("link to self"),
                                        linkWithRel("users-delete")
                                                .description("link to delete a user"),
                                        linkWithRel("users-me")
                                                .description("link to show user's info"),
                                        linkWithRel("profile")
                                                .description("link to describe it by itself")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT)
                                                .description("It accepts MediaType.APPLICATION_JSON"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("Its contentType is MediaType.APPLICATION_JSON")
                                ),
                                requestFields(
                                        fieldWithPath("email")
                                                .description("user's email and it should be unique"),
                                        fieldWithPath("password")
                                                .description("user's password"),
                                        fieldWithPath("accessToken")
                                                .type(JsonFieldType.NULL)
                                                .description("accessToken which the user will get"),
                                        fieldWithPath("tokenType")
                                                .type(JsonFieldType.NULL)
                                                .description("tokenType which the user will get")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("The contentType is MediaType.APPLICATION_JSON")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken")
                                                .type(JsonFieldType.STRING)
                                                .description("accessToken to be issued to a user"),
                                        fieldWithPath("tokenType")
                                                .type(JsonFieldType.STRING)
                                                .description("It should be Bearer in this application"),
                                        fieldWithPath("_links.self.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to self"),
                                        fieldWithPath("_links.users-delete.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to delete a user"),
                                        fieldWithPath("_links.users-me.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to show user's info"),
                                        fieldWithPath("_links.profile.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to describe it by itself")
                                )
                        ));
    }

    @Test
    public void 사용자_정보_조회하기() throws Exception {
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        userService.createUser(requestView);
        String token = jwtTokenProvider.createToken(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(requestView.toEntity());

        mockMvc.perform(get(USER_BASE_URI + "/me")
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("users-me",
                                links(halLinks(),
                                        linkWithRel("self")
                                                .description("link to self"),
                                        linkWithRel("users-delete")
                                                .description("link to delete a user"),
                                        linkWithRel("profile")
                                                .description("link to describe it by itself")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT)
                                                .description("It accepts MediaType.APPLICATION_JSON"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("Its contentType is MediaType.APPLICATION_JSON"),
                                        headerWithName(HttpHeaders.AUTHORIZATION)
                                                .description("It has the combination of accessToken and tokenType values")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("The contentType is MediaType.APPLICATION_JSON")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NULL)
                                                .description("users's Id"),
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("The email address of the valid user"),
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("The user's name of the valid user"),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("The password of the valid user"),
                                        fieldWithPath("_links.self.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to self"),
                                        fieldWithPath("_links.users-delete.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to delete a user"),
                                        fieldWithPath("_links.profile.href")
                                                .type(JsonFieldType.STRING)
                                                .description("link to describe it by itself")
                                )
                        ));
    }
}