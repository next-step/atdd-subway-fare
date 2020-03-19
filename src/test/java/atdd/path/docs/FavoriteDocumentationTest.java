package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.TestConstant;
import atdd.path.application.FavoriteService;
import atdd.path.application.dto.*;
import atdd.path.domain.Station;
import atdd.path.web.FavoriteController;
import atdd.user.domain.User;
import atdd.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.TEST_USER_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
public class FavoriteDocumentationTest extends AbstractDocumentationTest {

    public static final String FAVORITE_URI = "/favorites";
    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib29yd29uaWVAZW1haWwuY29tIiwiaWF0IjoxNTgxOTg1NjYzLCJleHAiOjE1ODE5ODkyNjN9.nL07LEhgTVzpUdQrOMbJq-oIce_idEdPS62hB2ou2hg";

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        given(userRepository.findUserByEmail(any())).willReturn(User.builder()
                .email(TEST_USER_EMAIL)
                .name(TestConstant.TEST_USER_NAME)
                .password(TestConstant.TEST_USER_PASSWORD)
                .build());
    }

    @Test
    void createFavoriteStation() throws Exception {
        given(favoriteService.createStationFavorite(any(), any()))
                .willReturn(FavoriteStationResponseView.builder()
                        .id(1L)
                        .station(new Station(TestConstant.STATION_ID, TestConstant.STATION_NAME))
                        .build());

        String input = "{\"stationId\":" + TestConstant.STATION_ID + "}";

        this.mockMvc
                .perform(post("/favorites/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN))
                        .content(input)
                )
                .andExpect(status().isCreated())
                .andDo(document("favorite/station/create",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        requestFields(
                                fieldWithPath("stationId").type(JsonFieldType.NUMBER).description("The station's id for favorite")
                        ),
                        responseFields(
                                this.stationResponse()
                        )))
                .andDo(print());
    }

    @Test
    void findFavoriteStation() throws Exception {
        given(favoriteService.findFavoriteStation(any()))
                .willReturn(FavoriteStationsResponseView.builder()
                        .favoriteStations(Arrays.asList(FavoriteStationResponseView.builder()
                                .id(1L)
                                .station(new Station(TestConstant.STATION_ID, TestConstant.STATION_NAME))
                                .build()))
                        .build());
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/favorites/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN))
                )
                .andExpect(status().isOk())
                .andDo(document("favorite/station/find",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        responseFields(
                                fieldWithPath("favoriteStations").description("An array Favorite-station"))
                                .andWithPrefix("favoriteStations[].", this.stationResponse())
                ))
                .andDo(print());
    }

    @Test
    void deleteFavoriteStation() throws Exception {
        this.mockMvc
                .perform(RestDocumentationRequestBuilders.delete("/favorites/station/{id}", 1)
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN))
                )
                .andExpect(status().isNoContent())
                .andDo(document("favorite/station/delete",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        pathParameters(
                                parameterWithName("id").description("The id of favorite-station")
                        )
                ))
                .andDo(print());
    }

    @Test
    void createFavoriteRoute() throws Exception {
        String request = "{\"sourceStationId\":" + TestConstant.STATION_ID + ", \"targetStationId\":" + TestConstant.STATION_ID_2 + "}";
        given(favoriteService.createRouteFavorite(any(), any(), any()))
                .willReturn(FavoriteRouteResponseView.builder().id(1L)
                        .sourceStation(ItemView.builder().id(TestConstant.STATION_ID).name(TestConstant.STATION_NAME).build())
                        .targetStation(ItemView.builder().id(TestConstant.STATION_ID_2).name(TestConstant.STATION_NAME_2).build())
                        .build());

        this.mockMvc
                .perform(post(FAVORITE_URI + "/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN))
                        .content(request)
                )
                .andDo(document("favorite/route/create",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        requestFields(
                                fieldWithPath("sourceStationId").type(JsonFieldType.NUMBER).description("The start station's id for favorite route"),
                                fieldWithPath("targetStationId").type(JsonFieldType.NUMBER).description("The end station's id for favorite route")
                        ),
                        responseFields(
                                this.favoriteRouteResponse()
                        )
                ))
                .andDo(print());
    }

    @Test
    void findFavoriteRoute() throws Exception {
        given(favoriteService.findFavoriteRoute(any()))
                .willReturn(FavoriteRoutesResponseView.builder()
                        .favoriteRoutes(Arrays.asList(
                                FavoriteRouteResponseView.builder()
                                        .id(1L)
                                        .sourceStation(ItemView.builder()
                                                .id(TestConstant.STATION_ID)
                                                .name(TestConstant.STATION_NAME)
                                                .build())
                                        .targetStation(ItemView.builder()
                                                .id(TestConstant.STATION_ID_2)
                                                .name(TestConstant.STATION_NAME_2)
                                                .build())
                                        .build())).build());

        this.mockMvc
                .perform(get(FAVORITE_URI + "/route")
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN)))
                .andExpect(status().isOk())
                .andDo(document("favorite/route/find",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        responseFields(
                                fieldWithPath("favoriteRoutes").description("An array Favorite-route"))
                                .andWithPrefix("favoriteRoutes[]", this.favoriteRouteResponse())
                ))
                .andDo(print());
    }

    @Test
    void deleteFavoriteRoute() throws Exception {
        this.mockMvc
                .perform(RestDocumentationRequestBuilders.delete(FAVORITE_URI + "/route/{id}", 1)
                        .header("Authorization", String.format("Bearer %s", TEST_USER_TOKEN)))
                .andExpect(status().isNoContent())
                .andDo(document("favorite/route/delete",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        pathParameters(
                                parameterWithName("id").description("The id of favorite-station")
                        )
                ))
                .andDo(print());
    }

    private List<FieldDescriptor> stationResponse() {
        return Arrays.asList(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                fieldWithPath("station.id").type(JsonFieldType.NUMBER).description("The station's id for favorite"),
                fieldWithPath("station.name").type(JsonFieldType.STRING).description("The station's name for favorite"));
    }

    private List<FieldDescriptor> favoriteRouteResponse() {
        return Arrays.asList(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The created favorite route's id"),
                fieldWithPath("sourceStation.id").type(JsonFieldType.NUMBER).description("The start station's id for favorite route"),
                fieldWithPath("sourceStation.name").type(JsonFieldType.STRING).description("The start station's name for favorite route"),
                fieldWithPath("targetStation.id").type(JsonFieldType.NUMBER).description("The end station's id favorite route"),
                fieldWithPath("targetStation.name").type(JsonFieldType.STRING).description("The end station's name for favorite route"));
    }
}