package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import atdd.path.web.PathController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static atdd.TestConstant.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
public class GraphDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private GraphService graphService;

    @DisplayName("최단 거리 경로 조회를 할 수 있다")
    @Test
    void beAbleToShortestDistancePath() throws Exception {
        List<Station> stations = List.of(TEST_STATION, TEST_STATION_2, TEST_STATION_3);
        given(graphService.findPath(STATION_ID, STATION_ID_3)).willReturn(stations);

        mockMvc.perform(get("/paths")
                .queryParam("startId", String.valueOf(STATION_ID))
                .queryParam("endId", String.valueOf(STATION_ID_3))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("/paths",
                                requestParameters(
                                        parameterWithName("startId").description("The start station's id"),
                                        parameterWithName("endId").description("The end station's id")
                                ),
                                responseFields(
                                        fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The start station's id"),
                                        fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description("The end station's id"),
                                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The station's id"),
                                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The station's name"),
                                        fieldWithPath("stations[].lines").type(JsonFieldType.ARRAY).description("The station's lines").optional()
                                )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startStationId").value(STATION_ID))
                .andExpect(jsonPath("$.endStationId").value(STATION_ID_3))
                .andExpect(jsonPath("$.stations.length()").value(3));
    }

}
