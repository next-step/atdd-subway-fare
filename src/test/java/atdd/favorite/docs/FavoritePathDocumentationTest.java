package atdd.favorite.docs;

import atdd.BaseDocumentationTest;
import atdd.favorite.application.dto.*;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.favorite.service.FavoritePathService;
import atdd.path.application.GraphService;
import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
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
import java.util.Optional;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.favorite.FavoriteConstant.FAVORITE_PATH_BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoritePathDocumentationTest extends BaseDocumentationTest {
    private FavoritePath FAVORITE_PATH_1 = new FavoritePath(1L, EMAIL, 1L, 2L);
    private FavoritePath FAVORITE_PATH_2 = new FavoritePath(2L, EMAIL, 2L, 3L);
    private FavoritePath FAVORITE_PATH_3 = new FavoritePath(3L, EMAIL, 3L, 4L);
    private Edge EDGE = new Edge(1L, STATION_1, STATION_2, DISTANCE_KM);
    private Edge EDGE_2 = new Edge(1L, STATION_2, STATION_3, DISTANCE_KM);
    private Edges EDGES = new Edges(Arrays.asList(EDGE, EDGE_2));
    private Line LINE = new Line(1L, NAME, START_TIME, END_TIME, INTERVAL_MIN);
    private FavoritePathRequestView requestView;
    private FavoritePathResponseView responseView;
    private FavoritePathResponseResource resource;
    private FavoritePathListResponseView listResponseView;
    private FavoritePathListResponseResource listResource;
    private User user;
    private String inputJson;
    private String token;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public FavoritePathDocumentationTest(JwtTokenProvider jwtTokenProvider,
                                         ObjectMapper objectMapper,
                                         MockMvc mockMvc) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @MockBean
    UserService userService;

    @MockBean
    FavoritePathService favoritePathService;

    @MockBean
    FavoritePathRepository favoritePathRepository;

    @MockBean
    GraphService graphService;

    @Test
    void 문서화_지하철경로_즐겨찾기_등록하기() throws Exception {
        //given
        requestForOneFavoritePath(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(user);
        given(favoritePathService.create(any(FavoritePathRequestView.class)))
                .willReturn(responseView);

        //when, then
        mockMvc.perform(post(FAVORITE_PATH_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("favorite-path-create",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-path-delete")
                                        .description("link to delete a favorite-path"),
                                linkWithRel("favorite-path-showAll")
                                        .description("link to show all favorite-paths"),
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
                                        .type(JsonFieldType.NULL)
                                        .description("It should be NULL"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("startId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to start"),
                                fieldWithPath("endId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to end"),
                                fieldWithPath("sameStation")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("It tells whether the station to start " +
                                                "and the station to end is same or not")
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
                                fieldWithPath("favoritePathStations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the stations in a favorite-path"),
                                fieldWithPath("favoritePathStations[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station in favorite-path"),
                                fieldWithPath("favoritePathStations[0].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the station in a favorite-path"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-path-delete.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to delete a favorite-path"),
                                fieldWithPath("_links.favorite-path-showAll.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite-paths"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    @Test
    void 문서화_지하철경로_즐겨찾기_삭제하기() throws Exception {
        //given
        requestForOneFavoritePath(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(user);
        given(favoritePathRepository.findById(anyLong()))
                .willReturn(Optional.of(FAVORITE_PATH_1));

        //when, then
        mockMvc.perform(delete(FAVORITE_PATH_BASE_URI + "/" + responseView.getId())
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorite-path-delete",
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
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NULL)
                                        .description("It should be NULL"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.NULL)
                                        .description("It should be NULL"),
                                fieldWithPath("favoritePathStations")
                                        .type(JsonFieldType.NULL)
                                        .description("It should be NULL"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    @Test
    void 문서화_지하철경로_즐겨찾기_목록_불러오기() throws Exception {
        //given
        requestForThreeFavoritePaths(EMAIL);
        given(userService.findByEmail(EMAIL)).willReturn(user);
        given(favoritePathService.showAllFavoritePath(any(FavoritePathRequestView.class)))
                .willReturn(listResponseView);

        //when, then
        mockMvc.perform(get(FAVORITE_PATH_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorite-path-showAll",
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
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user"),
                                fieldWithPath("favoritePaths")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the favorite-paths"),
                                fieldWithPath("favoritePaths[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of a favorite-path in favorite-path list"),
                                fieldWithPath("favoritePaths[0].email")
                                        .type(JsonFieldType.STRING)
                                        .description("The email address of the user who registered this"),
                                fieldWithPath("favoritePaths[0].startId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to start"),
                                fieldWithPath("favoritePaths[0].endId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to end"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }

    void requestForOneFavoritePath(String email) throws Exception {
        token = jwtTokenProvider.createToken(email);
        user = new User(email, NAME, PASSWORD);
        requestView = new FavoritePathRequestView(email, stationId1, stationId3);
        responseView = new FavoritePathResponseView(1L, email,
                Arrays.asList(STATION_1, STATION_2, STATION_3));
        resource = new FavoritePathResponseResource(responseView);
        inputJson = objectMapper.writeValueAsString(requestView);
    }

    void requestForThreeFavoritePaths(String email) throws Exception {
        token = jwtTokenProvider.createToken(email);
        user = new User(email, NAME, PASSWORD);
        requestView = new FavoritePathRequestView(email, stationId1, stationId3);
        listResponseView = new FavoritePathListResponseView(email,
                Arrays.asList(FAVORITE_PATH_1, FAVORITE_PATH_2, FAVORITE_PATH_3));
        listResource = new FavoritePathListResponseResource(listResponseView);
        inputJson = objectMapper.writeValueAsString(requestView);
    }
}
