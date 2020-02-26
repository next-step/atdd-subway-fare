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

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.Constant.USER_BASE_URI;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    public void loginTest() throws Exception {
        LoginRequestView loginRequestView = new LoginRequestView(EMAIL, PASSWORD);
        String inputJson = objectMapper.writeValueAsString(loginRequestView);
        given(userService.findByEmail(EMAIL)).willReturn(new User(NAME, EMAIL, PASSWORD));

        mockMvc.perform(
                post(Constant.LOGIN_BASE_URI)
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.users-delete.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(
                        document("login",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("It accepts MediaType.APPLICATION_JSON"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Its contentType is MediaType.APPLICATION_JSON")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("The contentType is MediaType.APPLICATION_JSON")
                                )
                        ))
                .andDo(print());

    }

    @Test
    public void showUserInfo() throws Exception {
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        userService.createUser(requestView);
        String token = jwtTokenProvider.createToken(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(requestView.toEntity());

        mockMvc.perform(get(USER_BASE_URI + "/me")
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.password").value(PASSWORD))
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.users-delete.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(print())
                .andDo(document("users-me",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("The contentType is MediaType.APPLICATION_JSON")
                        )
                ));
    }
}