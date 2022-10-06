package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        when(pathService.findPath(any(), any())).thenReturn(경로_생성());

        RestAssured
                .given(spec).log().all()
                .filter(document("paths",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 ID"),
                                parameterWithName("target").description("도착역 ID"),
                                parameterWithName("type").description("경로를 구하는 기준(거리/소요시간)")),
                        responseFields(
                                subsectionWithPath("stations.[].id").description("지하철역 ID"),
                                subsectionWithPath("stations.[].name").description("지하철역 이름"),
                                subsectionWithPath("distance").description("거리"),
                                subsectionWithPath("duration").description("소요시간"),
                                subsectionWithPath("fare").description("요금"))
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    private PathResponse 경로_생성() {
        List<StationResponse> 지하철역_목록 = List.of(
                StationResponse.of(new Station(1L, "교대역")),
                StationResponse.of(new Station(3L, "강남역")),
                StationResponse.of(new Station(2L, "잠실역")));

        return new PathResponse(지하철역_목록, 20, 10, 1450);
    }
}
