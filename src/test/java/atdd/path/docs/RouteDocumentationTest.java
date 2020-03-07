package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.TestConstant;
import atdd.path.application.RouteService;
import atdd.path.application.dto.RouteResponseDto;
import atdd.path.application.dto.StationResponseDto;
import atdd.path.web.RouteController;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
public class RouteDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private RouteService routeService;

    @Test
    void distance() throws Exception {
        given(routeService.findShortestDistancePath(anyLong(), anyLong()))
                .willReturn(RouteResponseDto.builder()
                        .startStationId(1L)
                        .endStationId(2L)
                        .stations(Arrays.asList(StationResponseDto.builder()
                                .id(TestConstant.STATION_ID)
                                .name(TestConstant.STATION_NAME)
                                .lines(Sets.newHashSet())
                                .build()))
                        .estimatedTime(8)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/routes/distance?startId=1&endId=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("routes/distance",
                        requestParameters(
                                parameterWithName("startId").description("The start station id"),
                                parameterWithName("endId").description("The end station Id")
                        ),
                        responseFields(
                                fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The start station id"),
                                fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description("The end station Id"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The route stations's id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The route stations's name"),
                                fieldWithPath("stations[].lines").type(JsonFieldType.ARRAY).description("The route stations's lines"),
                                fieldWithPath("estimatedTime").type(JsonFieldType.NUMBER).description("The total estimated Time (minutes)")
                        )))
                .andDo(print());
    }

    @Test
    void time() throws Exception {
        given(routeService.findShortestTimePath(anyLong(), anyLong()))
                .willReturn(RouteResponseDto.builder()
                        .startStationId(1L)
                        .endStationId(2L)
                        .stations(Arrays.asList(StationResponseDto.builder()
                                .id(TestConstant.STATION_ID)
                                .name(TestConstant.STATION_NAME)
                                .lines(Sets.newHashSet())
                                .build()))
                        .estimatedTime(8)
                        .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/routes/time?startId=1&endId=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("routes/time",
                        requestParameters(
                                parameterWithName("startId").description("The start station id"),
                                parameterWithName("endId").description("The end station Id")
                        ),
                        responseFields(
                                fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The start station id"),
                                fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description("The end station Id"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The route stations's id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The route stations's name"),
                                fieldWithPath("stations[].lines").type(JsonFieldType.ARRAY).description("The route stations's lines"),
                                fieldWithPath("estimatedTime").type(JsonFieldType.NUMBER).description("The total estimated Time (minutes)")
                        )))
                .andDo(print());
    }
}
