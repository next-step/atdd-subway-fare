package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.TestConstant;
import atdd.path.application.StationService;
import atdd.path.application.dto.StationTimetableDto;
import atdd.path.domain.Timetables;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import atdd.path.web.StationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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
    private StationService stationService;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private LineRepository lineRepository;

    @DisplayName("역 시간표 조회")
    @Test
    void retrieveStationTimetable() throws Exception {
        given(stationService.retrieveStationTimetable(TestConstant.STATION_ID))
                .willReturn(Arrays.asList(StationTimetableDto.builder()
                        .lineId(TestConstant.LINE_ID_2)
                        .lineName(TestConstant.LINE_NAME)
                        .timetables(Timetables.builder()
                                .up(Arrays.asList(LocalTime.of(5, 0)))
                                .down(Arrays.asList(LocalTime.of(5, 4)))
                                .build())
                        .build()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/stations/{stationId}/timetables", TestConstant.STATION_ID))
                .andExpect(status().isOk())
                .andDo(document("stations/timetable",
                        pathParameters(
                                parameterWithName("stationId").description("The station id for timetable")
                        ),
                        responseFields(
                                this.timetableResponse()
                        )))
                .andDo(print());
    }

    private List<FieldDescriptor> timetableResponse() {
        return Arrays.asList(
                fieldWithPath("[].lineId").type(JsonFieldType.NUMBER).description("The line Id"),
                fieldWithPath("[].lineName").type(JsonFieldType.STRING).description("The line name"),
                fieldWithPath("[].timetables").type(JsonFieldType.OBJECT).description("The station timetable in line"),
                fieldWithPath("[].timetables.up").type(JsonFieldType.ARRAY).description("The up line timetable"),
                fieldWithPath("[].timetables.down").type(JsonFieldType.ARRAY).description("The down line timetable"));
    }
}
