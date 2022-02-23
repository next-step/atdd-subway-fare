package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.documentation.DocumentationHelper.노선_생성_성공;
import static nextstep.subway.documentation.DocumentationHelper.노선_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    private LineResponse lineResponse;

    @BeforeEach
    void setFixtures() {
        List<StationResponse> stationResponses = new ArrayList<>();
        Station 강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        Station 역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);

        stationResponses.add(StationResponse.of(강남역));
        stationResponses.add(StationResponse.of(역삼역));
        lineResponse = new LineResponse(1L, "2호선", "green", stationResponses, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void 노선_생성() {
        when(lineService.saveLine(any())).thenReturn(lineResponse);
        ExtractableResponse<Response> response = 노선_생성_요청(spec);
        노선_생성_성공(response);
    }
}
