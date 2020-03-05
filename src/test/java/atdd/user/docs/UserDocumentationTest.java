package atdd.user.docs;

import atdd.AbstractDocumentationTest;
import atdd.security.JwtTokenProvider;
import atdd.user.application.UserService;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import atdd.user.web.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static atdd.TestConstant.*;
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

@WebMvcTest(UserController.class)
public class UserDocumentationTest extends AbstractDocumentationTest {
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void create() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME);

        String inputJson = "{\"email\":\"" + user.getEmail() + "\"," +
                "\"password\":\"" + user.getPassword() + "\"," +
                "\"name\":\"" + user.getName() + "\"}";

        given(userService.createUser(any())).willReturn(UserResponseView.of(user));

        this.mockMvc.perform(post("/users")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document("users/create",
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
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getUserEmail(any())).willReturn(TEST_USER_EMAIL);
        given(userService.retrieveUser(anyString()))
                .willReturn(UserResponseView.of(new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME)));

        this.mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andDo(
                        document("users/me",
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer auth credentials")),
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
