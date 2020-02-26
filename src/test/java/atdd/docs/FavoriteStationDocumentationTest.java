package atdd.docs;

import atdd.Constant;
import atdd.favorite.application.FavoriteStationService;
import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.path.dao.StationDao;
import atdd.path.domain.Station;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.Constant.FAVORITE_STATION_BASE_URI;
import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
    @DisplayName("지하철역 즐겨찾기 등록하기")
    public void createFavoriteStationTest() throws Exception {
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
                .andDo(document("favorite-station-create"));
    }

    @Test
    @DisplayName("지하철역 즐겨찾기 삭제하기")
    public void deleteFavoriteStationTest() throws Exception {
        //given
        FavoriteStationResponseView responseView = createFavoriteStationForTest(STATION_NAME_13);

        //when, then
        mockMvc.perform(
                delete(FAVORITE_STATION_BASE_URI + "/" + responseView.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-station-delete"));
    }

    @Test
    @DisplayName("지하철역 즐겨찾기 목록보기")
    public void showAllFavoriteStations() throws Exception {
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
                .andDo(document("favorite-station-showAllFavoriteStations"));
    }

    public FavoriteStationResponseView createFavoriteStationForTest(String stationName) {
        Station station = stationDao.save(new Station(stationName));
        CreateFavoriteStationRequestView requestView = new CreateFavoriteStationRequestView(EMAIL, station.getId());
        return favoriteStationService.createFavoriteStation(requestView);
    }
}