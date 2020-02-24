package atdd.docs;

import atdd.Constant;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    @Test
    public void loginTest() throws Exception {
        LoginRequestView loginRequestView = new LoginRequestView(EMAIL, PASSWORD, jwtTokenProvider);
        String token = jwtTokenProvider.createToken(EMAIL);
        String inputJson = objectMapper.writeValueAsString(loginRequestView);

        mockMvc.perform(
                post(Constant.LOGIN_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").exists())
                .andDo(print())
                .andDo(document("login",
                        links(halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("users-me").description("link to show user's info")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Its contentType is MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("It tells if the user has a token to access some information")
                        ),
                        requestFields(
                                fieldWithPath("email").description("user's email and it should be unique"),
                                fieldWithPath("password").description("user's password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location ends with user's id"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("The token the user has"),
                                fieldWithPath("tokenType").type(JsonFieldType.STRING).description("The token type we issued"),
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("link to self"),
                                fieldWithPath("_links.users-me.href").type(JsonFieldType.STRING).description("link to show user's info")
                        )
                ));
    }
}
