package atdd.docs;

import atdd.Constant;
import atdd.favorite.application.FavoritePathService;
import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.path.application.LineService;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.Constant.FAVORITE_PATH_BASE_URI;
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
public class FavoritePathDocumentationTest extends BaseDocumentationTest {
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line;
    private String token;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    LineService lineService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    FavoritePathService favoritePathService;

    @Test
    public void 지하철경로_즐겨찾기_등록하기() throws Exception {
        //given
        setUpForPathTest(LINE_NAME_5);
        CreateFavoritePathRequestView requestView
                = new CreateFavoritePathRequestView(EMAIL_2, station1.getId(), station5.getId());
        String inputJson = objectMapper.writeValueAsString(requestView);

        //when, then
        mockMvc.perform(
                post(Constant.FAVORITE_PATH_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("userEmail").exists())
                .andExpect(jsonPath("favoritePath").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-path-create",
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
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-path"),
                                fieldWithPath("userEmail")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoritePath")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the registered favorite path "),
                                fieldWithPath("favoritePath[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favoriteStation"),
                                fieldWithPath("favoritePath[0].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the user"),
                                fieldWithPath("favoritePath[0].lines")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the lines of the station"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    @Test
    public void 지하철경로_즐겨찾기_삭제하기() throws Exception {
        //given
        setUpForPathTest(LINE_NAME_7);
        FavoritePathResponseView favoritePaths
                = createFavoritePaths(EMAIL_2, station1.getId(), station5.getId());

        //when, then
        mockMvc.perform(
                delete(FAVORITE_PATH_BASE_URI + "/" + favoritePaths.getId())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-path-delete",
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
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the favorite-path"),
                                fieldWithPath("userEmail")
                                        .type(JsonFieldType.NULL)
                                        .description("The email address of the user"),
                                fieldWithPath("favoritePath")
                                        .type(JsonFieldType.NULL)
                                        .description("The list of the registered favorite-path "),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    @Test
    public void 지하철경로_즐겨찾기_목록보기() throws Exception {
        //given
        setUpForPathTest(LINE_NAME_6);
        createFavoritePaths(EMAIL_2, station1.getId(), station5.getId());
        createFavoritePaths(EMAIL_2, station2.getId(), station5.getId());
        createFavoritePaths(EMAIL_2, station4.getId(), station3.getId());
        createFavoritePaths(EMAIL_2, station4.getId(), station1.getId());
        createFavoritePaths(EMAIL_2, station3.getId(), station5.getId());

        //when, then
        mockMvc.perform(
                get(FAVORITE_PATH_BASE_URI)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("favoritePaths.[3].userEmail").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-path-showAllFavoritePaths",
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
                                fieldWithPath("favoritePaths")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the registered favorite-path "),
                                fieldWithPath("favoritePaths[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The of the favorite-path-Id in the list "),
                                fieldWithPath("favoritePaths[0].userEmail")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoritePaths[0].startStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the startStationId in a favorite-path"),
                                fieldWithPath("favoritePaths[0].endStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the endStationId in a favorite-path"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    public void setUpForPathTest(String lineName) {
        token = jwtTokenProvider.createToken(EMAIL_2);
        station1 = stationDao.save(new Station(STATION_NAME_31));
        station2 = stationDao.save(new Station(STATION_NAME_32));
        station3 = stationDao.save(new Station(STATION_NAME_33));
        station4 = stationDao.save(new Station(STATION_NAME_34));
        station5 = stationDao.save(new Station(STATION_NAME_35));
        line = lineDao.save(Line.of(lineName, START_TIME, END_TIME, INTERVAL_MIN));
        lineService.addEdge(line.getId(), station1.getId(), station2.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station2.getId(), station3.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station3.getId(), station4.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station4.getId(), station5.getId(), DISTANCE_KM);
    }

    public FavoritePathResponseView createFavoritePaths(String email, Long startId, Long endId) {
        CreateFavoritePathRequestView requestView = new CreateFavoritePathRequestView(email, startId, endId);
        return favoritePathService.create(requestView);
    }
}