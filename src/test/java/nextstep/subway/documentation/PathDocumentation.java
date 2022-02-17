package nextstep.subway.documentation;

import com.google.common.collect.Lists;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.두_역의_최적_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", null, null),
                        new StationResponse(2L, "역삼역", null, null)
                ), 10, 10
        );

        when(pathService.findPath(anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        RequestSpecification spec = this.spec.filter(
                document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 Id"),
                                parameterWithName("target").description("도착역 Id"),
                                parameterWithName("type").description("최적 경로 조회 기준")
                        ),
                        responseFields(
                                subsectionWithPath("stations").description("경로 내 있는 역 목록"),
                                fieldWithPath("distance").description("경로 거리"),
                                fieldWithPath("duration").description("경로 소요 시간"))
                )
        );

        두_역의_최적_경로_조회를_요청(1L, 2L, PathType.DISTANCE.name(), spec);
    }
}
