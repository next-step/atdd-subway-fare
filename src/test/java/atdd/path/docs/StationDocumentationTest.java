package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.web.StationController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
public class StationDocumentationTest extends AbstractDocumentationTest {
    @MockBean
    private StationDao stationDao;

    @MockBean
    private LineDao lineDao;

    @Test
    void showTimetables() throws Exception {
        //given
        List<Edge> edges1 = Arrays.asList(TEST_EDGE, TEST_EDGE_2, TEST_EDGE_3, TEST_EDGE_4); //2호선
        List<Edge> edges2 = Arrays.asList(TEST_EDGE_5, TEST_EDGE_6, TEST_EDGE_7, TEST_EDGE_8); //신분당선

        Line line1 = new Line(LINE_ID, LINE_NAME, edges1, LocalTime.of(5, 45), LocalTime.of(00, 05), 10);
        Line line2 = new Line(LINE_ID_2, LINE_NAME_2, edges2, LocalTime.of(5, 45), LocalTime.of(00, 05), 10);

        Station station = new Station(STATION_ID, STATION_NAME, Arrays.asList(line1, line2));

        given(stationDao.findById(anyLong())).willReturn(station);
        given(lineDao.findByIds(anyList())).willReturn(Arrays.asList(line1, line2));

        //when
        ResultActions result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/stations/{id}/timetables", station.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(
                        document("station/showTimetables",
                                pathParameters(
                                        parameterWithName("id").description(" The Station's id")
                                ),
                                responseFields(
                                        fieldWithPath("[].lineId").type(JsonFieldType.NUMBER).description("The line's id"),
                                        fieldWithPath("[].lineName").type(JsonFieldType.STRING).description("The line's name"),
                                        fieldWithPath("[].timetables.up[]").type(JsonFieldType.ARRAY).description("The station's up line timeTable"),
                                        fieldWithPath("[].timetables.down[]").type(JsonFieldType.ARRAY).description("The station's down line timeTable")
                                )
                        ))
                .andDo(print());
    }
}
