package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.acceptance.step.LineSteps;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static nextstep.subway.acceptance.step.LineSteps.구간_추가_요청_생성;
import static nextstep.subway.documentation.step.LineDocumentSteps.노선_생성_문서화;
import static nextstep.subway.documentation.step.LineSectionDocumentSteps.구간_생성_문서화;
import static org.mockito.ArgumentMatchers.any;

public class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    private int DISTANCE = 100;
    private int DURATION = 10;

    private Line 신분당선;
    private Station 강남역;
    private Station 판교역;
    private Station 정자역;

    @BeforeEach
    void setUp() {
        강남역 = 역_생성(1L, "강남역");
        판교역 = 역_생성(2L, "판교역");
        정자역 = 역_생성(3L, "정자역");

        신분당선 = 노선_생성(1L, "신분당선", "red", 강남역, 판교역, 100, 10, Fare.of(BigDecimal.valueOf(1000)));
    }

    @DisplayName("구간 추가 문서화")
    @Test
    void addSection() {
        Map<String, String> 요청 = 구간_추가_요청_생성(강남역.getId(), 판교역.getId(), DISTANCE, DURATION);

        RestAssured
                .given(spec).log().all()
                .filter(구간_생성_문서화())
                .body(요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", 신분당선.getId())
                .then().log().all().extract();
    }

    @DisplayName("노선 생성 문서화")
    @Test
    void createLine() {
        Map<String, String> 요청 = LineSteps.노선_생성_Param_생성(신분당선.getName(), 신분당선.getColor(), 강남역.getId(), 판교역.getId(), 100, 10, BigDecimal.ZERO);
        LineResponse 응답 = LineResponse.of(신분당선);
        Mockito.when(lineService.saveLine(any()))
                .thenReturn(응답);

        RestAssured
                .given(spec).log().all()
                .filter(노선_생성_문서화())
                .body(요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private Station 역_생성(long id, String name) {
        LocalDateTime now = LocalDateTime.now();
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        ReflectionTestUtils.setField(station, "createdDate", now);
        ReflectionTestUtils.setField(station, "modifiedDate", now);
        return station;
    }

    private Line 노선_생성(long id, String name, String color, Station upStation, Station downStation, int distance, int duration, Fare fare) {
        LocalDateTime now = LocalDateTime.now();
        Line line = Line.of(name, color, upStation, downStation, distance, duration, fare);
        ReflectionTestUtils.setField(line, "id", id);
        ReflectionTestUtils.setField(line, "createdDate", now);
        ReflectionTestUtils.setField(line, "modifiedDate", now);

        return line;
    }

}
