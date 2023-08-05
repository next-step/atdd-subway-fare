package nextstep.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static nextstep.api.subway.acceptance.station.StationSteps.지하철역_생성_요청;
import static nextstep.api.subway.acceptance.station.StationSteps.지하철역_전체조회_요청;
import static nextstep.api.subway.acceptance.station.StationSteps.지하철역_제거_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;

import nextstep.api.subway.applicaion.station.StationService;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.documentation.Documentation;

@ExtendWith(RestDocumentationExtension.class)
class StationDocumentation extends Documentation {
    @MockBean
    private StationService stationService;

    private final String name = "강남역";
    private final StationResponse response = new StationResponse(1L, name);

    @Test
    void createStation() {
        when(stationService.saveStation(any())).thenReturn(response);

        지하철역_생성_요청(name, makeRequestSpec("station-create"));
    }

    @Test
    void deleteStation() {
        doNothing().when(stationService).deleteStationById(anyLong());

        지하철역_제거_요청(1L, makeRequestSpec("station-delete"));
    }

    @Test
    void showStations() {
        when(stationService.findAllStations()).thenReturn(List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "삼성역"),
                new StationResponse(3L, "선릉역")
        ));

        지하철역_전체조회_요청(makeRequestSpec("station-show"));
    }
}
