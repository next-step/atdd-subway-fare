package nextstep.subway.station.documentation;

import nextstep.subway.Documentation;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.station.documentation.StationSteps.getSpec;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StationDocumentation extends Documentation {
    private Map<String, String> params;
    private String 강남역 = "강남역";
    private String 역삼역 = "역삼역";
    private StationResponse 생성된_강남역;
    private StationResponse 생성된_역삼역;

    @MockBean
    private StationService stationService;

    @BeforeEach
    void setUp() {
        params = new HashMap<>();
        params.put("name", 강남역);
        생성된_강남역 = new StationResponse(
                1L, 강남역, LocalDateTime.now(), LocalDateTime.now()
        );
        생성된_역삼역 = new StationResponse(
                2L, 역삼역, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        when(stationService.saveStation(any())).thenReturn(생성된_강남역);

        getSpec(spec, "station_create")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        when(stationService.findAllStations()).thenReturn(Lists.newArrayList(생성된_강남역, 생성된_역삼역));

        getSpec(spec, "station_search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/stations")
                .then().log().all();
    }
}
