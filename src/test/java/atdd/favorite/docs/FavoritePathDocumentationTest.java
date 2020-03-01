package atdd.favorite.docs;

import atdd.BaseDocumentationTest;
import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseResource;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.domain.FavoritePath;
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

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.favorite.FavoriteConstant.FAVORITE_PATH_BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoritePathDocumentationTest extends BaseDocumentationTest {
    private FavoritePath FAVORITE_PATH_1 = new FavoritePath(1L, EMAIL, 1L, 2L);
    private FavoritePath FAVORITE_PATH_2 = new FavoritePath(2L, EMAIL, 2L, 3L);
    private FavoritePath FAVORITE_PATH_3 = new FavoritePath(3L, EMAIL, 3L, 4L);
    private Edge EDGE = new Edge(1L, STATION_1, STATION_2, DISTANCE_KM);
    private Edge EDGE_2 = new Edge(1L, STATION_2, STATION_3, DISTANCE_KM);
    private Edges EDGES = new Edges(Arrays.asList(EDGE, EDGE_2));
    private Line LINE = new Line(1L, NAME,START_TIME, END_TIME, INTERVAL_MIN);
    private FavoritePathRequestView requestView;
    private FavoritePathResponseView responseView;
    private FavoritePathResponseResource resource;
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
    GraphService graphService;

    @Test
    void 문서화__지하철경로_즐겨찾기_등록하기() throws Exception {
        //given
        token = jwtTokenProvider.createToken(EMAIL);
        user = new User(EMAIL, NAME, PASSWORD);
        requestView = new FavoritePathRequestView(EMAIL);
        responseView = new FavoritePathResponseView(1L, EMAIL, Arrays.asList(STATION_1, STATION_2, STATION_3));
        resource = new FavoritePathResponseResource(responseView);
        inputJson = objectMapper.writeValueAsString(requestView);
        given(userService.findByEmail(EMAIL)).willReturn(user);
        given(favoritePathService.create(any(FavoritePathRequestView.class)))
                .willReturn(responseView);

        //when, then
        mockMvc.perform(post(FAVORITE_PATH_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER+token)
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
                                        .description("link to show all favorite-paths")
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
                                fieldWithPath("startId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to start"),
                                fieldWithPath("endId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to end")
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
                                fieldWithPath("favoritePathStations[0].lines")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the lines that the station belongs to"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-path-delete.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to delete a favorite-path"),
                                fieldWithPath("_links.favorite-path-showAll.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to show all favorite-paths")
                        )
                ));
    }
}
