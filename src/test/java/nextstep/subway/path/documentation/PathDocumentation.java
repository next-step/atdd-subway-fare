package nextstep.subway.path.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {

        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now())
                        , new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 아이디"),
                                parameterWithName("target").description("도착역 아이디"),
                                parameterWithName("type").description("DISTANCE : 최소거리 / DURATION : 최소시간")
                                ),
                        responseFields(
                                fieldWithPath("stations[]").description("역 정보"),
                                fieldWithPath("stations[].id").description("역 아이디"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("stations[].createdDate").description("생성일시"),
                                fieldWithPath("stations[].modifiedDate").description("수정일시"),
                                fieldWithPath("distance").description("구간의 거리"),
                                fieldWithPath("duration").description("구간의 소요시간")
                )))
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}

