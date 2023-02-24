package nextstep.subway.documentation;

import static nextstep.subway.applicaion.dto.SearchType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.steps.PathSteps;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

public class PathDocumentation extends Documentation {

	@MockBean
	private PathService pathService;

	@Test
	void path() {
		// given
		PathResponse pathResponse = new PathResponse(
			Lists.newArrayList(
				new StationResponse(1L, "강남역"),
				new StationResponse(2L, "역삼역")
			), 10, 5
		);
		when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

		// when
		ExtractableResponse<Response> searchResponse = PathSteps.searchPathDistance(spec, 1L, 2L, DISTANCE);

		// then
		assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
