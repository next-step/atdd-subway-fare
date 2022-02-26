package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathDocumentationTemplate.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.WeightType;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", null, null),
                new StationResponse(2L, "역삼역", null, null)
            ), 10, 5, 1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestDocumentationFilter 경로_조회_문서 = 경로_조회_템플릿();

        경로_조회(spec, 경로_조회_문서);
    }

    public static void 경로_조회(RequestSpecification spec, RestDocumentationFilter filter) {
        RestAssured
            .given(spec).log().all()
            .filter(filter)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParams("weightType", WeightType.DISTANCE)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
