package nextstep.subway.documentation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.PathSteps;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @BeforeEach
    void setup() {
        PathResponse pathResponse = new PathResponse(List.of(
            new StationResponse(1L, "교대역"),
            new StationResponse(2L, "남부터미널역")
        ), 10);

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);
    }

    @Test
    void path() {
        spec.filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
        ExtractableResponse<Response> response = PathSteps.지하철_경로_조회(spec, 1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
