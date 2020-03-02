package atdd.favorite.docs;

import atdd.BaseDocumentationTest;
import atdd.favorite.application.dto.*;
import atdd.favorite.domain.FavoriteStation;
import atdd.favorite.service.FavoriteStationService;
import atdd.user.application.UserService;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.favorite.FavoriteConstant.FAVORITE_STATION_BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoriteStationDocumentationTest extends BaseDocumentationTest {
    private FavoriteStation FAVORITE_STATION_1 = new FavoriteStation(1L, EMAIL, stationId1);
    private FavoriteStation FAVORITE_STATION_2 = new FavoriteStation(2L, EMAIL, stationId2);
    private FavoriteStation FAVORITE_STATION_3 = new FavoriteStation(3L, EMAIL, stationId3);
    private FavoriteStationRequestView requestView;
    private FavoriteStationResponseView responseView;
    private FavoriteStationResponseResource resource;
    private FavoriteStationListResponseVIew listResponseVIew;
    private FavoriteStationListResponseResource listResource;
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
    void 문서화_지하철역_즐겨찾기_등록하기() throws Exception {
        //given
        requestForOneFavoriteStation(EMAIL, stationId1);
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
    void 문서화_지하철역_즐겨찾기_삭제하기() throws Exception {
        //given
        requestForOneFavoriteStation(EMAIL, stationId1);
        given(favoriteStationService.create(any())).willReturn(responseView);
        given(userService.findByEmail(EMAIL)).willReturn(user);

        //when
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

    @Test
    void 문서화_지하철역_즐겨찾기_목록보기() throws Exception {
        //given
        requestForThreeFavoriteStations(EMAIL);
        given(favoriteStationService.create(any()))
                .willReturn(responseView);
        given(userService.findByEmail(EMAIL))
                .willReturn(user);
        given(favoriteStationService.showAllFavoriteStations(any(FavoriteStationRequestView.class)))
                .willReturn(listResponseVIew);

        //when, then
        mockMvc.perform(get(FAVORITE_STATION_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorite-station-showAll",
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
                                        .description("Its contentType is MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.AUTHORIZATION)
                                        .description("It has the token to check if the user is valid")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The user email"),
                                fieldWithPath("favoriteStations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the favorite station of the user"),
                                fieldWithPath("favoriteStations[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-station"),
                                fieldWithPath("favoriteStations[0].email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address who registered it"),
                                fieldWithPath("favoriteStations[0].stationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station registered on favorite-station"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    void requestForOneFavoriteStation(String email, Long stationId) throws Exception {
        token = jwtTokenProvider.createToken(email);
        user = new User(email, NAME, PASSWORD);
        requestView = new FavoriteStationRequestView(email);
        responseView = new FavoriteStationResponseView(1L, email, stationId);
        resource = new FavoriteStationResponseResource(responseView);
        inputJson = objectMapper.writeValueAsString(requestView);
    }

    void requestForThreeFavoriteStations(String email) throws Exception {
        token = jwtTokenProvider.createToken(email);
        user = new User(email, NAME, PASSWORD);
        requestView = new FavoriteStationRequestView(email);
        listResponseVIew = new FavoriteStationListResponseVIew(email,
                Arrays.asList(FAVORITE_STATION_1, FAVORITE_STATION_2, FAVORITE_STATION_3));
        listResource = new FavoriteStationListResponseResource(listResponseVIew);
        inputJson = objectMapper.writeValueAsString(requestView);
    }
}
