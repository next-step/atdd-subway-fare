package nextstep.subway.steps;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionCreateRequest;

public class SectionSteps {

	public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Long upStationId, Long downStationId, int distance, int duration) {
		SectionCreateRequest sectionRequest = new SectionCreateRequest(upStationId, downStationId, distance, duration);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(sectionRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines/{id}/sections", lineId)
			.then().log().all()
			.extract();

		return response;
	}
}
