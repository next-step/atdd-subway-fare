package atdd.docs;

import atdd.Constant;
import atdd.path.application.LineService;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PathDocumentationTest extends BaseDocumentationTest {
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    LineService lineService;

    @Test
    @DisplayName("최소 경로 조회하기")
    public void findPathTest() throws Exception {
        //given
        setUpForPathTest();

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

    public void setUpForPathTest() {
        station1 = stationDao.save(new Station(STATION_NAME_23));
        station2 = stationDao.save(new Station(STATION_NAME_24));
        station3 = stationDao.save(new Station(STATION_NAME_25));
        station4 = stationDao.save(new Station(STATION_NAME_26));
        station5 = stationDao.save(new Station(STATION_NAME_27));
        line = lineDao.save(Line.of(LINE_NAME_3, START_TIME, END_TIME, INTERVAL_MIN));
        lineService.addEdge(line.getId(), station1.getId(), station2.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station2.getId(), station3.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station3.getId(), station4.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station4.getId(), station5.getId(), DISTANCE_KM);
    }
}