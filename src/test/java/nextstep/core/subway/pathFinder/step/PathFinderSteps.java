package nextstep.core.subway.pathFinder.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderSteps {

    public static ExtractableResponse<Response> 성공하는_지하철_경로_조회_요청(PathFinderRequest pathFinderRequest) {
        return given()
                .param("source", pathFinderRequest.getDepartureStationId())
                .param("target", pathFinderRequest.getArrivalStationId())
                .param("type", pathFinderRequest.getPathFinderType())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 실패하는_지하철_경로_조회_요청(PathFinderRequest pathFinderRequest) {
        given()
                .param("source", pathFinderRequest.getDepartureStationId())
                .param("target", pathFinderRequest.getArrivalStationId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    public static int convertToDistance(ExtractableResponse<Response> 성공하는_경로_조회_응답) {
        return 성공하는_경로_조회_응답.jsonPath().getInt("distance");
    }

    public static int convertToDuration(ExtractableResponse<Response> 성공하는_경로_조회_응답) {
        return 성공하는_경로_조회_응답.jsonPath().getInt("duration");
    }

    public static List<Long> convertToStationIds(ExtractableResponse<Response> 성공하는_경로_조회_응답) {
        return 성공하는_경로_조회_응답.jsonPath().getList("stations.id", Long.class);
    }

    public static void 경로에_포함된_역_목록_검증(ExtractableResponse<Response> 성공하는_경로_조회_응답, Long... 역_번호_목록) {
        assertThat(convertToStationIds(성공하는_경로_조회_응답)).containsExactly(역_번호_목록);
    }

    public static void 경로에_포함된_최단거리_검증(ExtractableResponse<Response> 성공하는_경로_조회_응답, int 예상_최단거리) {
        assertThat(convertToDistance(성공하는_경로_조회_응답)).isEqualTo(예상_최단거리);
    }

    public static void 경로에_포함된_소요_시간_검증(ExtractableResponse<Response> 성공하는_경로_조회_응답, int 예상_소요_시간) {
        assertThat(convertToDuration(성공하는_경로_조회_응답)).isEqualTo(예상_소요_시간);
    }
}
