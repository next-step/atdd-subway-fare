package nextstep.acceptance.commonStep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathStep {

    public static ExtractableResponse<Response> 지하철_경로_조회(Long sourceStationId, Long targetStationId, RequestSpecification restAssured){

        ExtractableResponse<Response> response =
                restAssured.given()
                        .when()
                        .get("/paths?source=" + sourceStationId + "&target=" + targetStationId)
                        .then().log().all()
                        .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철_경로_조회(Long sourceStationId, Long targetStationId){

        return 지하철_경로_조회(sourceStationId,targetStationId,RestAssured.given());
    }
}
