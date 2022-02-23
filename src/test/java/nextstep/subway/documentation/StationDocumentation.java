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
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.documentation.DocumentationHelper.역_목록_조회_성공;
import static nextstep.subway.documentation.DocumentationHelper.역_목록_조회_요청;
import static nextstep.subway.documentation.DocumentationHelper.역_삭제_성공;
import static nextstep.subway.documentation.DocumentationHelper.역_삭제_요청;
import static nextstep.subway.documentation.DocumentationHelper.역_생성_성공;
import static nextstep.subway.documentation.DocumentationHelper.역_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class StationDocumentation extends Documentation {

    @MockBean
    private StationService stationService;
    private List<StationResponse> responses = new ArrayList<>();
    private StationRequest stationRequest;
    private StationResponse stationResponse;
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @BeforeEach
    void setFixtures() {
        stationRequest = new StationRequest(강남역);
        stationResponse = new StationResponse(1L, 강남역, LocalDateTime.now(), LocalDateTime.now());
        responses.add(stationResponse);
        responses.add(new StationResponse(2L, 역삼역, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void 역_생성() {
        when(stationService.saveStation(any())).thenReturn(stationResponse);
        ExtractableResponse<Response> response = 역_생성_요청(spec, stationRequest);
        역_생성_성공(response);
    }

    @Test
    void 역_목록_조회() {
        when(stationService.findAllStations()).thenReturn(responses);
        ExtractableResponse<Response> response = 역_목록_조회_요청(spec);
        역_목록_조회_성공(response);
    }

    @Test
    void 역_삭제() {
        doNothing().when(stationService).deleteStationById(anyLong());
        ExtractableResponse<Response> response = 역_삭제_요청(spec);
        역_삭제_성공(response);
    }
}
