package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.application.GraphService;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.web.PathController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
public class GraphDocumentationTest extends AbstractDocumentationTest {
    @MockBean
    private GraphService graphService;

    @Test
    void paths() throws Exception {
        //given
        Station station1 = new Station(STATION_ID_11, STATION_NAME_11, Arrays.asList(TEST_LINE_3));
        Station station2 = new Station(STATION_ID_12, STATION_NAME_12, Arrays.asList(TEST_LINE, TEST_LINE_3));
        Station station3 = new Station(STATION_ID, STATION_NAME, Arrays.asList(TEST_LINE));
        Station station4 = new Station(STATION_ID_2, STATION_NAME_2, Arrays.asList(TEST_LINE));
        Station station5 = new Station(STATION_ID_3, STATION_NAME_3, Arrays.asList(TEST_LINE));
        Station station6 = new Station(STATION_ID_4, STATION_NAME_4, Arrays.asList(TEST_LINE));

        List<Station> stations = Arrays.asList(station1, station2, station3, station4, station5, station6);

        given(graphService.findPath(anyLong(), anyLong())).willReturn(stations);

        long sourceStationId = TEST_STATION_11.getId();
        long targetStationId = TEST_STATION_4.getId();

        //when
        ResultActions result = this.mockMvc.perform(get("/paths?startId=" + sourceStationId + "&endId=" + targetStationId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("paths", pathsParameters(), pathsFields()))
                .andDo(print());
    }

    @Test
    void findMinTimePath() throws Exception {
        List<Station> stations = Arrays.asList(TEST_STATION_11, TEST_STATION_12, TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4);
        List<Line> lines = Arrays.asList(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);

        given(graphService.findMinTimePath(anyLong(), anyLong())).willReturn(stations);
        given(graphService.findAllLine()).willReturn(lines);

        //when
        ResultActions result = this.mockMvc.perform(get("/paths/min-time?startId=" + STATION_ID_11 + "&endId=" + STATION_ID_4)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(document("paths/min-time", findMinTimePathParameters(), findMinTimePathFields()))
                .andDo(print());
    }

    private RequestParametersSnippet pathsParameters() {
        return requestParameters(
                parameterWithName("startId").description("start station's ID"),
                parameterWithName("endId").description("end station's ID"));
    }

    private ResponseFieldsSnippet pathsFields() {
        return responseFields(
                fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The start station's id"),
                fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description(" The end station's id"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The station's id"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The station's name"),
                fieldWithPath("stations[].lines[].id").type(JsonFieldType.NUMBER).description("The line's id"),
                fieldWithPath("stations[].lines[].name").type(JsonFieldType.STRING).description("The line's name"));
    }

    private RequestParametersSnippet findMinTimePathParameters() {
        return requestParameters(
                parameterWithName("startId").description("start station's ID"),
                parameterWithName("endId").description("end station's ID"));
    }

    private ResponseFieldsSnippet findMinTimePathFields() {
        return responseFields(
                fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The start station's id"),
                fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description(" The end station's id"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The station's id"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The station's name"),
                fieldWithPath("lines[].id").type(JsonFieldType.NUMBER).description("The line's id"),
                fieldWithPath("lines[].name").type(JsonFieldType.STRING).description("The line's name"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("The distance"),
                fieldWithPath("departAt").type(JsonFieldType.STRING).description("The departAt"),
                fieldWithPath("arriveBy").type(JsonFieldType.STRING).description("The arriveBy"));
    }
}
