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

public class PathSteps {

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
					fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간(분)")
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

	public static ExtractableResponse<Response> 타입에_따라_두_역의_경로_조회를_요청(Long source, Long target, SearchType type) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
			.then().log().all().extract();
	}
}
