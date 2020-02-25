package atdd.docs;

import atdd.path.application.GraphService;
import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
    EdgeDao edgeDao;

    @Autowired
    GraphService graphService;

    @Test
    public void findPathTest() throws Exception {
        //given
        Station station1 = stationDao.save(new Station(STATION_NAME));
        Station station2 = stationDao.save(new Station(STATION_NAME_2));
        Station station3 = stationDao.save(new Station(STATION_NAME_3));
        Station station4 = stationDao.save(new Station(STATION_NAME_4));
        Station station5 = stationDao.save(new Station(STATION_NAME_5));
        Line line = lineDao.save(Line.of(LINE_NAME, START_TIME, END_TIME, INTERVAL_MIN));
        edgeDao.save(line.getId(), Edge.of(station1, station2, DISTANCE_KM));
        edgeDao.save(line.getId(), Edge.of(station2, station3, DISTANCE_KM));
        edgeDao.save(line.getId(), Edge.of(station3, station4, DISTANCE_KM));
        edgeDao.save(line.getId(), Edge.of(station4, station5, DISTANCE_KM));

        //when, then
        mockMvc.perform(
                get("/paths?startId=" + station2.getId() + "&endId=" + station5.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startStationId").exists())
                .andExpect(jsonPath("$.endStationId").exists())
                .andExpect(jsonPath("$.stations.length()").exists())
                .andDo(print())
                .andDo(document("find-path",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("startStationId").type(JsonFieldType.STRING).description("The id of the station to start"),
                                fieldWithPath("endStationId").type(JsonFieldType.STRING).description("The id of the station to end"),
                                fieldWithPath("stations.length()").type(JsonFieldType.NUMBER).description("The number of the stations in the path")
                        )
                ));
    }
}
