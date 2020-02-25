package atdd.docs;

import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoritePathRepository;
import atdd.path.application.LineService;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.user.domain.User;
import atdd.user.domain.UserRepository;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static atdd.Constant.AUTH_SCHEME_BEARER;
import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
public class FavoritePathDocumentationTest {
    public static final String FAVORITE_PATH_BASE_URI = "/favorite-paths";
    public static final String NAME = "brown";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    public static final int INTERVAL_MIN = 10;
    public static final int DISTANCE_KM = 5;
    public static final LocalTime START_TIME = LocalTime.of(5, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 55);
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line;
    private String token;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FavoritePathRepository favoritePathRepository;

    @Autowired
    LineDao lineDao;

    @Autowired
    LineService lineService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StationDao stationDao;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken(EMAIL);
        userRepository.save(new User(NAME, EMAIL, PASSWORD));
        station1 = stationDao.save(new Station(STATION_NAME_20));
        station2 = stationDao.save(new Station(STATION_NAME_21));
        station3 = stationDao.save(new Station(STATION_NAME_19));
        station4 = stationDao.save(new Station(STATION_NAME_18));
        station5 = stationDao.save(new Station(STATION_NAME_17));
        line = lineDao.save(Line.of(LINE_NAME_2, START_TIME, END_TIME, INTERVAL_MIN));
        lineService.addEdge(line.getId(), station1.getId(), station2.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station2.getId(), station3.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station3.getId(), station4.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station4.getId(), station5.getId(), DISTANCE_KM);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        stationDao.deleteById(station1.getId());
        stationDao.deleteById(station2.getId());
        stationDao.deleteById(station3.getId());
        stationDao.deleteById(station4.getId());
        stationDao.deleteById(station5.getId());
        lineDao.deleteById(line.getId());
    }

    @Test
    public void createFavoritePathTest() throws Exception {
        //given
        CreateFavoritePathRequestView requestView
                = new CreateFavoritePathRequestView(EMAIL, station1.getId(), station5.getId());
        String inputJson = objectMapper.writeValueAsString(requestView);

        //when, then
        mockMvc.perform(
                post(FAVORITE_PATH_BASE_URI)
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
                .andDo(document("favorite-path-create"));
    }

    @Test
    public void deleteFavoritePathTest() throws Exception {
        //given
        FavoritePath favoritePath = favoritePathRepository.save(new FavoritePath(EMAIL, station1.getId(), station4.getId()));

        //when, then
        mockMvc.perform(
                delete(FAVORITE_PATH_BASE_URI + "/" + favoritePath.getId())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-path-delete"));
    }

    @Test
    public void showAllFavoritePaths() throws Exception{
        //given
        favoritePathRepository.save(new FavoritePath(EMAIL, station1.getId(), station4.getId()));
        favoritePathRepository.save(new FavoritePath(EMAIL, station2.getId(), station4.getId()));
        favoritePathRepository.save(new FavoritePath(EMAIL, station3.getId(), station4.getId()));
        favoritePathRepository.save(new FavoritePath(EMAIL, station2.getId(), station1.getId()));

        //when, then
        mockMvc.perform(
                get(FAVORITE_PATH_BASE_URI)
                .header(HttpHeaders.AUTHORIZATION, AUTH_SCHEME_BEARER+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("favoritePaths.[3].userEmail").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("favorite-path-showAllFavoritePaths"));
    }
}