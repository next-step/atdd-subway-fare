package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
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
        Station 정자역 = new Station("정자역");
        ReflectionTestUtils.setField(정자역, "id", 2L);

        stationResponses.add(StationResponse.of(강남역));
        stationResponses.add(StationResponse.of(정자역));

        Line line = createLine();
        lineResponse = new LineResponse(line, stationResponses);
    }

    @Test
    void 노선_생성() {
        when(lineService.saveLine(any())).thenReturn(lineResponse);
        ExtractableResponse<Response> response = 노선_생성_요청(spec);
        노선_생성_성공(response);
    }

    private Line createLine() {
        Line line = new Line();
        ReflectionTestUtils.setField(line, "id", 1L);
        ReflectionTestUtils.setField(line, "name", "신분당선");
        ReflectionTestUtils.setField(line, "color", "red");
        ReflectionTestUtils.setField(line, "extraCharge", 900);
        ReflectionTestUtils.setField(line, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(line, "modifiedDate", LocalDateTime.now());
        return line;
    }
}
