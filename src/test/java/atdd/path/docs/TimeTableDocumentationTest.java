package atdd.path.docs;

import atdd.BaseDocumentationTest;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeTableDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    private Line line_sinbundang
            = new Line(1L, "Line_Sinbundang",
            LocalTime.of(06, 00), LocalTime.of(8, 30), 50);
    private Line line_2
            = new Line(2L, "Line_2",
            LocalTime.of(05, 00), LocalTime.of(8, 50), 25);
    private Station station = new Station(1L, "Gangnam", Arrays.asList(line_2, line_sinbundang));
    private Station station2 = new Station(2L, "Yangjae", Arrays.asList(line_sinbundang));
    private Station station3 = new Station(3L, "Yoeksam", Arrays.asList(line_2));
    private Station station4 = new Station(4L, "Seoul Nat'l Univ. of Education", Arrays.asList(line_2));
    private Edge edge = new Edge(1L, station, station2, INTERVAL_MIN);
    private Edge edge2 = new Edge(2L, station, station3, INTERVAL_MIN);
    private Edge edge3 = new Edge(3L, station4, station, INTERVAL_MIN);

    @Autowired
    public TimeTableDocumentationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @MockBean
    StationDao stationDao;

    @MockBean
    LineDao lineDao;

    @Test
    void 문서화_지하철역_시간표_조회하기() throws Exception {
        //given
        line_2.addEdge(edge2);
        line_2.addEdge(edge3);
        line_sinbundang.addEdge(edge);
        given(stationDao.findById(station.getId())).willReturn(station);
        given(lineDao.findById(line_sinbundang.getId())).willReturn(line_sinbundang);
        given(lineDao.findById(line_2.getId())).willReturn(line_2);
        String inputJson = "{\"name\":\"" + station.getName() + "\"}";

        //when, then
        mockMvc.perform(get("/stations/" + station.getId() + "/timetables")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("station-timetables",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("[].lineId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the line where the station belongs"),
                                fieldWithPath("[].lineName")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the line where the station belongs"),
                                fieldWithPath("[].timeTables")
                                        .type(JsonFieldType.OBJECT)
                                        .description("The list of timetables of the subway at the station"),
                                fieldWithPath("[].timeTables.up")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The timetable of the station to go up"),
                                fieldWithPath("[].timeTables.down")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The timetable of the station to go down")
                        )));
    }
}
