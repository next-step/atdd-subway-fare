package atdd.docs;

import atdd.Constant;
import atdd.path.application.LineService;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
public class PathDocumentationTest {
    public static final String NAME = "brown";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String EMAIL2 = "brown@email.com";
    public static final String PASSWORD = "subway";
    public static final LocalTime START_TIME = LocalTime.of(5, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 55);
    public static final int INTERVAL_MIN = 10;
    public static final int DISTANCE_KM = 5;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    LineService lineService;

    @Test
    public void findPathTest() throws Exception {
        //given
        Station station1 = stationDao.save(new Station(STATION_NAME));
        Station station2 = stationDao.save(new Station(STATION_NAME_2));
        Station station3 = stationDao.save(new Station(STATION_NAME_3));
        Station station4 = stationDao.save(new Station(STATION_NAME_4));
        Station station5 = stationDao.save(new Station(STATION_NAME_5));
        Line line = lineDao.save(Line.of(LINE_NAME, START_TIME, END_TIME, INTERVAL_MIN));
        lineService.addEdge(line.getId(), station1.getId(), station2.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station2.getId(), station3.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station3.getId(), station4.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station4.getId(), station5.getId(), DISTANCE_KM);

        //when, then
        mockMvc.perform(
                get(Constant.PATH_BASE_URI)
                        .param("startId", station2.getId().toString())
                        .param("endId", station5.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startStationId").exists())
                .andExpect(jsonPath("$.endStationId").exists())
                .andExpect(jsonPath("$.stations.length()").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.favorite-paths").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("find-path"));
    }
}