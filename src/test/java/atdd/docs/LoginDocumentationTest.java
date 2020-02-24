package atdd.docs;

import atdd.Constant;
import atdd.user.application.UserService;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
public class LoginDocumentationTest {
    public static final String NAME = "brown";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String EMAIL2 = "brown@email.com";
    public static final String PASSWORD = "subway";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserService userService;

    @Test
    public void loginTest() throws Exception {
        LoginRequestView loginRequestView = new LoginRequestView(EMAIL, PASSWORD, jwtTokenProvider);
        String token = jwtTokenProvider.createToken(EMAIL);
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
}
