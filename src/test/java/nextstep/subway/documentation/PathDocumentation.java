package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathSteps.*;
import static nextstep.subway.documentation.PathStubs.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.subway.applicaion.PathService;

public class PathDocumentation extends Documentation {
	@MockBean
	private PathService pathService;

	@Test
	void path() {
		경로_조회_시_성공_응답을_반환(pathService);

		spec = spec.filter(document("path",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		경로_조회_요청(spec);
	}
}
