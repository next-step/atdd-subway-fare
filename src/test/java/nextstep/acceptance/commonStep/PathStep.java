package nextstep.acceptance.commonStep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.domain.subway.PathType;

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
}
