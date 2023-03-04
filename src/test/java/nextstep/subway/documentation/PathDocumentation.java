package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathSteps.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

public class PathDocumentation extends Documentation {
	@MockBean
	private PathService pathService;

	@Test
	void path() {
		PathResponse pathResponse = new PathResponse(
			Lists.newArrayList(
				new StationResponse(1L, "강남역"),
				new StationResponse(2L, "역삼역")
			), 10
		);

		when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

		spec = spec.filter(document("path",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())));

		경로_조회_요청(spec);
	}
}
