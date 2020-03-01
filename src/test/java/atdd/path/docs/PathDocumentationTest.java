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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
public class PathDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private GraphService graphService;

    @DisplayName("경로 찾기")
    @Test
    public void findPath() throws Exception {
        Station gangnam = new Station(1L, "강남역");
        Station yuksam = new Station(2L, "역삼역");
        Station seongreung = new Station(3L, "선릉역");

        List<Station> stations = new ArrayList<Station>();
        stations.add(gangnam);
        stations.add(yuksam);
        stations.add(seongreung);

        given(graphService.findPath(anyLong(), anyLong())).willReturn(stations);

        mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get(
                                "/paths?startId={startId}&endId={endId}",
                                gangnam.getId(),
                                seongreung.getId()
                        )
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(
                        document("paths/read",
                                requestParameters(
                                        parameterWithName("startId").description("The start station id"),
                                        parameterWithName("endId").description("The end station id")
                                ),
                                responseFields(
                                        fieldWithPath("startStationId").type(JsonFieldType.NUMBER)
                                                .description("The start station id"),
                                        fieldWithPath("endStationId").type(JsonFieldType.NUMBER)
                                                .description("The end station id"),
                                        fieldWithPath("stations").type(JsonFieldType.ARRAY)
                                                .description("stations between start station and end station")
                                )
                        )
                )
                .andDo(print());
    }
}
