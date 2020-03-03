package atdd.user.docs;

import atdd.AbstractDocumentationTest;
import atdd.user.application.UserService;
import atdd.user.domain.User;
import atdd.user.web.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserDocumentationTest extends AbstractDocumentationTest {
    public static final String HEADER_NAME = "Authorization";
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";

    @MockBean
    private UserService userService;

    @Test
    void create() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME);

        String inputJson = "{\"email\":\"" + user.getEmail() + "\"," +
                "\"password\":\"" + user.getPassword() + "\"," +
                "\"name\":\"" + user.getName() + "\"}";

        given(userService.createUser(any())).willReturn(user);

        this.mockMvc.perform(post("/users")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document("users/create",
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
    void login() throws Exception {
        //given
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME);

        String inputJson = "{\"email\":\"" + user.getEmail() + "\"," +
                "\"password\":\"" + user.getPassword() + "\"}";

        given(userService.login(TEST_USER_EMAIL, TEST_USER_PASSWORD)).willReturn(TEST_USER_TOKEN);

        //when
        ResultActions result = this.mockMvc.perform(post("/users/login")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNoContent())
                .andDo(
                        document("users/login",
                                responseHeaders(
                                        headerWithName(HEADER_NAME).description("Bearer auth credentials")
                                ),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                                )
                        ))
                .andDo(print());
    }

    @Test
    void me() throws Exception {
        given(userService.findUserByEmail(anyString())).willReturn(new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME));

        this.mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andDo(
                        document("users/me",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestHeaders(
                                        headerWithName(HEADER_NAME).description(
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