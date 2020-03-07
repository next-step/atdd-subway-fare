package atdd.user.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import atdd.path.web.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserDocumentationTest extends AbstractDocumentationTest {
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";

    @MockBean
    private UserRepository userRepository;

    @Test
    void create() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        String inputJson = "{\"email\":\"" + user.getEmail() + "\"," +
                "\"password\":\"" + user.getPassword() + "\"," +
                "\"name\":\"" + user.getName() + "\"}";

        given(userRepository.save(any())).willReturn(user);

        this.mockMvc.perform(post("/users")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document("users/create",
//                                links(linkWithRel("profile").description("Link to the profile resource")),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                                )
                        ))
                .andDo(print());
    }

    @Test
    void me() throws Exception {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new User(1L, TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME)));
        given(jwtTokenProvider.parseToken(anyString())).willReturn(TEST_USER_EMAIL);

        this.mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
                .andDo(
                        document("users/me",
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

    @DisplayName("사용자 목록 조회")
    @Test
    void retrieveUsers() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        List<User> users = new ArrayList<User>();
        users.add(user);

        given(userRepository.findAll()).willReturn(users);

        FieldDescriptor[] userField = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user id"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("The user email"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The user name").optional(),
                fieldWithPath("password").type(JsonFieldType.STRING).description("The user password").optional(),
        };

        mockMvc
                .perform(
                        get("/users").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(jsonPath("$[0].name").value(TEST_USER_NAME))
                .andDo(
                        document(
                                "users/read",
                                responseFields(
                                        fieldWithPath("[]")
                                                .description("An array of User")
                                ).andWithPrefix("[].", userField)
                        )
                )
                .andDo(print());
    }

    @DisplayName("유저 정보 조회")
    @Test
    public void retrieveUser() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        given(userRepository.findById(any())).willReturn(Optional.of(user));

        mockMvc
                .perform(
                        RestDocumentationRequestBuilders
                                .get("/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(TEST_USER_NAME))
                .andDo(
                        document(
                                "users/{id}/read",
                                pathParameters(
                                        parameterWithName("id").description("The user id to retrieve")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The user's id"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The user's name")
                                )
                        )
                );
    }

    @DisplayName("사용자 삭제")
    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders
                        .delete("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(
                        document(
                                "users/{id}/delete",
                                pathParameters(
                                        parameterWithName("id").description("The user id to delete")
                                )
                        )
                )
                .andDo(print());
    }

    @DisplayName("로그인")
    @Test
    public void signIn() throws Exception {
        User user = new User(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        String inputJson = "{\"email\":\"" + user.getEmail() + "\"," +
                "\"password\":\"" + user.getPassword() +"\"}";

        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createToken(any())).willReturn(TEST_USER_TOKEN);

        mockMvc.perform(post("/users/login")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(TEST_USER_TOKEN))
                .andDo(
                        document(
                                "user/signIn",
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("The user's email address"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("The user's password")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The user's access token"),
                                        fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The access token type")
                                )
                        )
                );
    }
}