package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.경로_조회하기_문서화_스펙_정의;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    @Test
    void pathTypeDistance() {
        spec = 경로_조회하기_문서화_스펙_정의(restDocumentation);

        StationResponse 교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);

        지하철_노선_생성_요청("이호선", "이호선색상", 교대역.getId(), 양재역.getId(), 20, 10);

        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역.getId())
            .queryParam("target", 양재역.getId())
            .queryParam("pathType", PathType.DISTANCE)
            .when().get("/paths")
            .then().assertThat().statusCode(is(200))
            .log().all().extract();

        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getStations().size()).isEqualTo(2);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(10);
    }

}
