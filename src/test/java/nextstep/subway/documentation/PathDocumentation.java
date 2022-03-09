package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathInquiryType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void 최단_경로_조회() {
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(
                new StationResponse(1L, "강남역", null, null),
                new StationResponse(2L, "역삼역", null, null)
        ), 10, 5);

        when(pathService.findPath(anyLong(), anyLong(), eq(PathInquiryType.DISTANCE)))
                .thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path/distance",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", PathInquiryType.DISTANCE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    @Test
    void 최단_시간_조회() {
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
        ), 10, 5);

        when(pathService.findPath(anyLong(), anyLong(), eq(PathInquiryType.DURATION)))
                .thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path/duration",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", PathInquiryType.DURATION)
                .when().get("/paths")
                .then().log().all().extract();
    }

}
