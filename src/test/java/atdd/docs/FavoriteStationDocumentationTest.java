package atdd.docs;

import atdd.Constant;
import atdd.favorite.application.FavoriteStationService;
import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.path.dao.StationDao;
import atdd.path.domain.Station;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.Constant.FAVORITE_STATION_BASE_URI;
import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class FavoriteStationDocumentationTest extends BaseDocumentationTest {
    private String token;

    @Autowired
    StationDao stationDao;

    @Autowired
    FavoriteStationService favoriteStationService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken(EMAIL);

    }

    @Test
    public void 지하철역_즐겨찾기_등록하기() throws Exception {
        //given
        Station station = stationDao.save(new Station(STATION_NAME_8));
        CreateFavoriteStationRequestView requestView = new CreateFavoriteStationRequestView(EMAIL, station.getId());
        String inputJson = objectMapper.writeValueAsString(requestView);

        //when, then
        mockMvc.perform(
                post(Constant.FAVORITE_STATION_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("userEmail").exists())
                .andExpect(jsonPath("favoriteStationId").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.favorite-station-showAllStations").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-station-create",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-station-delete")
                                        .description("link to delete a favorite-station"),
                                linkWithRel("favorite-station-showAllStations")
                                        .description("link to show all favorite stations"),
                                linkWithRel("profile")
                                        .description("link to describe it by itself")
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
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-station"),
                                fieldWithPath("userEmail")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoriteStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to be registered "),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-station-delete.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to delete a favorite path"),
                                fieldWithPath("_links.favorite-station-showAllStations.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite stations"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    @Test
    public void 지하철역_즐겨찾기_삭제하기() throws Exception {
        //given
        FavoriteStationResponseView responseView = createFavoriteStationForTest(STATION_NAME_13);

        //when, then
        mockMvc.perform(
                delete(FAVORITE_STATION_BASE_URI + "/" + responseView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorite-station-delete",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-station-showAllStations")
                                        .description("link to show all favorite stations"),
                                linkWithRel("profile")
                                        .description("link to describe it by itself")
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
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-station"),
                                fieldWithPath("userEmail")
                                        .type(JsonFieldType.NULL)
                                        .description("The email address of the user"),
                                fieldWithPath("favoriteStationId")
                                        .type(JsonFieldType.NULL)
                                        .description("The id of the station to be registered "),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-station-showAllStations.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite stations"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    @Test
    public void 지하철역_즐겨찾기_목록보기() throws Exception {
        //given
        createFavoriteStationForTest(STATION_NAME_21);
        createFavoriteStationForTest(STATION_NAME_18);
        createFavoriteStationForTest(STATION_NAME_15);

        //when, then
        mockMvc.perform(
                get(FAVORITE_STATION_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("favoriteStations").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-station-showAllStations",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("profile")
                                        .description("link to describe it by itself")
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
                                fieldWithPath("userEmail")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoriteStations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the registered favorite stations"),
                                fieldWithPath("favoriteStations[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite stationId"),
                                fieldWithPath("favoriteStations[0].userEmail")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoriteStations[0].stationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station Id to have been registered"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )));
    }

    public FavoriteStationResponseView createFavoriteStationForTest(String stationName) {
        Station station = stationDao.save(new Station(stationName));
        CreateFavoriteStationRequestView requestView
                = new CreateFavoriteStationRequestView(EMAIL, station.getId());
        return favoriteStationService.createFavoriteStation(requestView);
    }
}