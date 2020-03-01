package atdd.user.docs;

import atdd.BaseDocumentationTest;
import atdd.user.application.dto.CreateUserRequestView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public UserDocumentationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void 문서화_회원_가입하기() throws Exception {
        //given
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        String inputJson = objectMapper.writeValueAsString(requestView);

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
}
