package nextstep.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.path.domain.PathType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps extends AcceptanceTestSteps {

    public static ExtractableResponse<Response> 경로를_조회한다(Long source, Long target, PathType type, RequestSpecification spec) {
        return spec
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type.name())
                .when().get("/paths")
                .then().log().all().extract();
    }


    public static void 경로_조회_정보가_일치한다(ExtractableResponse<Response> response, int distance, int duration, int fare, Long... stationIds) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }
}
