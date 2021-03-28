package nextstep.subway.path.documentation;

import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.path.documentation.PathSteps.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private PathResponse 강남_역삼_경로;

    @BeforeEach
    void setUp() {
        강남_역삼_경로 = new PathResponse(
                Lists.newArrayList(new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10
        );
    }

    @Test
    void path() {
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(강남_역삼_경로);

        getSpec(spec)
                .filter(document("path",
                        getRequestPreprocessor(), getResponsePreprocessor(),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("타입")),
                        responseFields(
                                fieldWithPath("stations").description("지하철 역 들"),
                                fieldWithPath("stations.[].id").description("지하철 역 아이디"),
                                fieldWithPath("stations.[].name").description("지하철 역 이름"),
                                fieldWithPath("stations.[].createdDate").description("지하철 역 생성시간"),
                                fieldWithPath("stations.[].modifiedDate").description("지하철 역 수정시간"),
                                fieldWithPath("distance").description("거리"),
                                fieldWithPath("duration").description("(걸린)시간"))))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all();
    }


}
