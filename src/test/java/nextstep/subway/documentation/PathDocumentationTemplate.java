package nextstep.subway.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathDocumentationTemplate {
	public static RestDocumentationFilter 경로_조회_템플릿() {
		return document("path",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			requestParameters(
				parameterWithName("source").description("출발역 아이디"),
				parameterWithName("target").description("도착역 아이디"),
				parameterWithName("weightType").description("조회 조건")
			),
			responseFields(
				fieldWithPath("stations").description("경로 지하철역 목록"),
				fieldWithPath("stations[].id").description("지하철역 아이디"),
				fieldWithPath("stations[].name").description("지하철역 이름"),
				fieldWithPath("stations[].createdDate").description("역 생성 일자"),
				fieldWithPath("stations[].modifiedDate").description("역 수정 일자"),
				fieldWithPath("distance").description("거리(km)"),
				fieldWithPath("duration").description("소요 시간(min)")
			)
		);
	}
}
