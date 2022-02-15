package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static nextstep.subway.acceptance.step.LineSteps.구간_추가_요청_생성;
import static nextstep.subway.documentation.step.LineSectionDocumentSteps.구간_생성_문서화;

public class LineSectionDocumentation extends Documentation {

    private int DISTANCE = 100;
    private int DURATION = 10;
    private Line 신분당선;
    private Station 강남역;
    private Station 판교역;
    private Station 정자역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(판교역, "id", 2L);
        ReflectionTestUtils.setField(정자역, "id", 3L);

        신분당선 = new Line("신분당선", "red");
        ReflectionTestUtils.setField(신분당선, "id", 1L);
    }

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

}
