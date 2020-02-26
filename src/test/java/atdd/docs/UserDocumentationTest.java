package atdd.docs;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static atdd.Constant.USER_BASE_URI;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDocumentationTest extends BaseDocumentationTest {
    @Autowired
    UserService userService;

    @Test
    void 회원_등록하기() throws Exception {
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        String inputJson = objectMapper.writeValueAsString(requestView);
        mockMvc.perform(
                post(USER_BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("users-create",
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
                                        fieldWithPath("name")
                                                .description("user's name"),
                                        fieldWithPath("password")
                                                .description("user's password")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION)
                                                .description("Location ends with user's id"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("The contentType is MediaType.APPLICATION_JSON")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("user's id"),
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("user's email and it should be unique"),
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("user's name"),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("user's password"),
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
    void 회원_삭제하기() throws Exception {
        CreateUserRequestView requestView = new CreateUserRequestView(EMAIL, NAME, PASSWORD);
        UserResponseView responseView = userService.createUser(requestView);

        mockMvc.perform(
                delete(USER_BASE_URI + "/" + responseView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(document("users-delete",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("users-create")
                                        .description("link to create a user"),
                                linkWithRel("profile")
                                        .description("link to describe it by itself")
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
                                        .type(JsonFieldType.NUMBER)
                                        .description("user's id to have been deleted"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.NULL)
                                        .description("it should be null in the response"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.NULL)
                                        .description("it should be null in the response"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.NULL)
                                        .description("user's password in the response"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.users-create.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to create a user"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }
}