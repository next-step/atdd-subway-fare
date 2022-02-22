package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static nextstep.subway.documentation.DocumentationHelper.역_생성_성공;
import static nextstep.subway.documentation.DocumentationHelper.역_생성_요청;
import static org.mockito.Mockito.when;

class StationDocumentation extends Documentation {

    @MockBean
    private StationService stationService;
    private StationRequest stationRequest;
    private StationResponse stationResponse;
    private static final String 강남역 = "강남역";

    @BeforeEach
    void setFixtures() {
        stationRequest = new StationRequest(강남역);
        stationResponse = new StationResponse(1L, 강남역, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void 역_등록() {
        when(stationService.saveStation(stationRequest)).thenReturn(stationResponse);
        ExtractableResponse<Response> response = 역_생성_요청(spec, stationRequest);
        역_생성_성공(response);
    }
}
