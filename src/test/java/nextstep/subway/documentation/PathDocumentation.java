package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathSteps.*;
import static nextstep.subway.documentation.PathStubs.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.subway.applicaion.PathService;

class PathDocumentation extends Documentation {
	@MockBean
	private PathService pathService;

	@DisplayName("최단 거리 경로 조회 DOCS - 200 OK")
	@Test
	void pathByDistance() {
		최단_거리_경로_조회_시_성공_응답을_반환(pathService);

		spec = spec.filter(document("path",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		최단_거리_경로_조회_요청(spec);
	}

	@DisplayName("최소 시간 경로 조회 DOCS - 200 OK")
	@Test
	void pathByDuration() {
		최소_시간_경로_조회_시_성공_응답을_반환(pathService);

		spec = spec.filter(document("path",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		최소_시간_경로_조회_요청(spec);
	}
}
