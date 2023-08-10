package nextstep.acceptance.commonStep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.domain.subway.PathType;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStep {

    public static ExtractableResponse<Response> 지하철_경로_조회(Long sourceStationId, Long targetStationId,PathType type, RequestSpecification restAssured){

        ExtractableResponse<Response> response =
                restAssured.given()
                        .when()
                        .get("/paths?source=" + sourceStationId + "&target=" + targetStationId+"&type="+type)
                        .then().log().all()
                        .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(Long sourceStationId, Long targetStationId, PathType type){

        return 지하철_경로_조회(sourceStationId,targetStationId,type,RestAssured.given());
    }

    public static void 지하철_경로_조회_검증(ExtractableResponse<Response> response, List<Long> expectedStation, Long distance,
                                    Long duration) {
        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                        .containsAll(expectedStation),
                () -> assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance),
                () -> assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration)
        );
    }
}
