package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.documentation.step.PathDocumentSteps;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static nextstep.subway.documentation.step.PathDocumentSteps.경로_조회_문서화;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private Station 강남역;
    private Station 판교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");

        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(판교역, "id", 2L);

    }

    @DisplayName("경로 조회 문서화")
    @Test
    void path() {
        PathResponse 응답 = PathDocumentSteps.경로_조회_응답_생성(Arrays.asList(강남역, 판교역));
        when(pathService.findPath(anyLong(), anyLong()))
                .thenReturn(응답);

        RestAssured
                .given(spec).log().all()
                .filter(경로_조회_문서화())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 강남역.getId())
                .queryParam("target", 판교역.getId())
                .when().get("/paths")
                .then().log().all().extract();
    }

}
