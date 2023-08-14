package nextstep.api.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import static nextstep.api.acceptance.subway.station.StationSteps.지하철역_생성_요청;
import static nextstep.api.acceptance.subway.station.StationSteps.지하철역_전체조회_요청;
import static nextstep.api.acceptance.subway.station.StationSteps.지하철역_제거_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.station.StationService;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class StationDocumentation extends Documentation {
    @MockBean
    private StationService stationService;

    @Test
    void createStation() {
        final var name = "강남역";
        when(stationService.saveStation(any())).thenReturn(new StationResponse(1L, name));

        지하철역_생성_요청(name, makeRequestSpec(
                document("station-create",
                        requestFields(
                                fieldWithPath("name").description("역 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").description("역 id"),
                                fieldWithPath("name").description("역 이름")
                        )
                ))
        );
    }

    @Test
    void deleteStation() {
        doNothing().when(stationService).deleteStationById(anyLong());

        지하철역_제거_요청(1L, makeRequestSpec(
                document("station-delete",
                        pathParameters(
                                parameterWithName("id").description("역 id")
                        )
                ))
        );
    }

    @Test
    void showStations() {
        when(stationService.findAllStations()).thenReturn(List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "삼성역"),
                new StationResponse(3L, "선릉역")
        ));

        지하철역_전체조회_요청(makeRequestSpec(
                document("station-show",
                        responseFields(
                                fieldWithPath("[].id").description("역 id"),
                                fieldWithPath("[].name").description("역 이름")
                        )
                ))
        );
    }
}
