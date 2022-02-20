package nextstep.subway.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "연신내역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "서울역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10,
                10
        );

        when(pathService.findPath(1L, 2L, PathType.DISTANCE)).thenReturn(pathResponse);

        RequestSpecification path = spec.filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
        경로_조회_요청(path);
    }
}
