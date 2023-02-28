package nextstep.subway.steps;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.dto.SearchType;

public class PathDocumentationSteps {

	public static ExtractableResponse<Response> searchPathDistance(RequestSpecification spec, Long source, Long target, SearchType type) {
		ExtractableResponse<Response> searchResponse = RestAssured
			.given(spec).log().all()
			.filter(document("path",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("source").description("출발역 id"),
					parameterWithName("target").description("도착역 id"),
					parameterWithName("type").description("조회 기준 (DISTANCE, DURATION)")
				),
				responseFields(
					fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
					fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 id"),
					fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
					fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리(km)"),
					fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간(분)"),
					fieldWithPath("fare").type(JsonFieldType.NUMBER).description("이용요금(원)")
				)))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("source", source)
			.queryParam("target", target)
			.queryParam("type", type)
			.when().get("/paths")
			.then().log().all()
			.extract();
		return searchResponse;
	}

}
