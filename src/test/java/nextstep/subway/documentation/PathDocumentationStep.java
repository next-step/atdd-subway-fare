package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentationStep {

	public static RequestSpecification 경로_조회_문서화(RequestSpecification documentSpecification) {
		return RestAssured
				.given(documentSpecification).log().all()
				.filter(document("path",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("source").description("출발역 아이디"),
								parameterWithName("target").description("도착역 아이디"),
								parameterWithName("type").description("거리기준/시간기준")
						),
						responseFields(
								subsectionWithPath("stations[]").description("출발역부터 도착역까지 역 경로"),
								fieldWithPath("distance").description("출발역과 도착역까지 총 거리"),
								fieldWithPath("duration").description("출발역과 도착역까지 소요시간"),
								fieldWithPath("fare").description("지하철 이용 요금")
						)
				));
	}
}
