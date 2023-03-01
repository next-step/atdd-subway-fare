package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathSearchRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("경로찾기 문서화 테스트")
@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void pathDefault() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(4L, "양재역")
                ), 10, 10, 1250
        );

        BDDMockito.given(pathService.findPath(any(PathSearchRequest.class), any())).willReturn(pathResponse);

        두_역의_최단_거리_경로_조회를_요청(1L, 4L, spec, defaultFilter("pathDefault"));
    }

    @Test
    void pathDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "교대역"),
                        new StationResponse(3L, "남부터미널"),
                        new StationResponse(4L, "양재역")
                ), 12, 5, 1350
        );

        BDDMockito.given(pathService.findPath(any(PathSearchRequest.class), any())).willReturn(pathResponse);

        두_역의_최소_시간_경로_조회를_요청(1L, 4L, spec, defaultFilter("pathDuration"));
    }
}
