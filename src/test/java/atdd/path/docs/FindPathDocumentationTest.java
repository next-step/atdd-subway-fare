package atdd.path.docs;

import atdd.BaseDocumentationTest;
import atdd.path.application.GraphService;
import atdd.path.application.dto.MinTimePathResponseView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindPathDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    List<Station> stations = Arrays.asList(TEST_STATION_6, TEST_STATION, TEST_STATION_2);
    Set<Line> lines = new HashSet<>();

    @Autowired
    public FindPathDocumentationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @MockBean
    GraphService graphService;

    @Test
    void 문서화_최소_경로_조회하기() throws Exception {
        //given
        lines.add(TEST_LINE); //2호선
        lines.add(TEST_LINE_2); //신분당선
        MinTimePathResponseView responseView
                = new MinTimePathResponseView(6L, 2L, stations, lines, 20,
                LocalTime.of(6, 00), LocalTime.of(6, 20));
        given(graphService.findMinTimePath(any(), any()))
                .willReturn(responseView);

        //when, then
        mockMvc.perform(get("/paths")
                .param("startId", STATION_ID_6.toString())  //신분당선-양재역
                .param("endId", STATION_ID_2.toString())    //2호선-역삼역
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("path-find",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("startStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to start"),
                                fieldWithPath("endStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to end"),
                                fieldWithPath("stations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the stations in the path"),
                                fieldWithPath("stations[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station in the path"),
                                fieldWithPath("stations[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the station in the path"),
                                fieldWithPath("lines")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the lines where the station belongs"),
                                fieldWithPath("lines[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the line where the station belongs"),
                                fieldWithPath("lines[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the line where the station belongs"),
                                fieldWithPath("distance")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The distance between the stations"),
                                fieldWithPath("departAt")
                                        .type(JsonFieldType.STRING)
                                        .description("The time to get on"),
                                fieldWithPath("arriveBy")
                                        .type(JsonFieldType.STRING)
                                        .description("The time to arrive at")
                        )
                ));
    }
}
