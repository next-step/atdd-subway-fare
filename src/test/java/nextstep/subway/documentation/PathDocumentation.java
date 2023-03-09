package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathSteps.*;
import static nextstep.subway.documentation.PathStubs.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import nextstep.subway.applicaion.PathService;

class PathDocumentation extends Documentation {
	@MockBean
	private PathService pathService;

	@DisplayName("최단 거리 경로 조회 DOCS - 200 OK")
	@Test
	void pathByDistance() {
		최단_거리_경로_조회_시_성공_응답을_반환(pathService);

		spec = spec.filter(경로_조회_필터("path/distance/success"));

		최단_거리_경로_조회_요청(spec);
	}

	@DisplayName("최소 시간 경로 조회 DOCS - 200 OK")
	@Test
	void pathByDuration() {
		최소_시간_경로_조회_시_성공_응답을_반환(pathService);

		spec = spec.filter(경로_조회_필터("path/duration/success"));

		최소_시간_경로_조회_요청(spec);
	}

	@DisplayName("최단 거리 경로 조회 DOCS - 400 BAD REQUEST")
	@Test
	void pathByDistance_fail() {
		최단_거리_경로_조회_시_실패_응답을_반환(pathService);

		spec = spec.filter(document("path/distance/fail",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		출발역과_도착역이_동일하게_최단_거리_경로_조회_요청(spec);
	}

	@DisplayName("최소 시간 경로 조회 DOCS - 400 BAD REQUEST")
	@Test
	void pathByDuration_fail() {
		최소_시간_경로_조회_시_실패_응답을_반환(pathService);

		spec = spec.filter(document("path/duration/fail",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		출발역과_도착역이_동일하게_최소_시간_경로_조회_요청(spec);
	}

	private RestDocumentationFilter 경로_조회_필터(String identifier) {
		return document(identifier,
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			requestParameters(
				parameterWithName("source").description("출발역 id"),
				parameterWithName("target").description("도착역 id"),
				parameterWithName("type").description("경로 조회 타입(DISTANCE, DURATION)")
			),
			responseFields(
				fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
				fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철역 id"),
				fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철역 이름"),
				fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리(km)"),
				fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간(분)"),
				fieldWithPath("fare").type(JsonFieldType.NUMBER).description("이용요금(원)")
			));
	}
}
