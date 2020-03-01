package atdd.favorite.docs;

import atdd.BaseDocumentationTest;
import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseResource;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.service.FavoriteStationService;
import atdd.user.application.UserService;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.favorite.FavoriteConstant.FAVORITE_STATION_BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoriteStationDocumentationTest extends BaseDocumentationTest {
    private FavoriteStationRequestView requestView;
    private FavoriteStationResponseView responseView;
    private FavoriteStationResponseResource resource;
    private User user;
    private String inputJson;
    private String token;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public FavoriteStationDocumentationTest(MockMvc mockMvc,
                                            JwtTokenProvider jwtTokenProvider,
                                            ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @MockBean
    UserService userService;

    @MockBean
    FavoriteStationService favoriteStationService;

    @Test
    void createForDocumentation() throws Exception {
        //given
        makeCreateRequest(EMAIL, stationId1);
        given(favoriteStationService.create(any())).willReturn(responseView);
        given(userService.findByEmail(EMAIL)).willReturn(user);

        //when, then
        mockMvc.perform(
                post(FAVORITE_STATION_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("favorite-station-create",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-station-delete")
                                        .description("link to delete a favorite-station"),
                                linkWithRel("favorite-station-showAll")
                                        .description("link to show all favorite stations"),
                                linkWithRel("profile")
                                        .description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.AUTHORIZATION)
                                        .description("It has the token to check if the user is valid")
                        ),
                        requestFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("It should be NULL"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("stationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to be registered ")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-station"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("stationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to be registered "),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-station-delete.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to delete a favorite path"),
                                fieldWithPath("_links.favorite-station-showAll.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite stations"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    @Test
    void deleteForDocumentation() throws Exception {
        makeCreateRequest(EMAIL, stationId1);
        given(favoriteStationService.create(any())).willReturn(responseView);
        given(userService.findByEmail(EMAIL)).willReturn(user);

        mockMvc.perform(delete(FAVORITE_STATION_BASE_URI + "/" + responseView.getId())
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorite-station-delete",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-station-showAll")
                                        .description("link to show all favorite stations"),
                                linkWithRel("profile")
                                        .description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.AUTHORIZATION)
                                        .description("It has the token to check if the user is valid")
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
                                        .type(JsonFieldType.NULL)
                                        .description("It should be null"),
                                fieldWithPath("stationId")
                                        .type(JsonFieldType.NULL)
                                        .description("It should be null"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-station-showAll.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite stations"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    void makeCreateRequest(String email, Long stationId) throws JsonProcessingException {
        token = jwtTokenProvider.createToken(email);
        user = new User(email, NAME, PASSWORD);
        requestView = new FavoriteStationRequestView(email, stationId);
        responseView = new FavoriteStationResponseView(1L, email, stationId);
        resource = new FavoriteStationResponseResource(responseView);
        inputJson = objectMapper.writeValueAsString(requestView);
    }
}
