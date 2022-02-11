package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("최단 경로 조회 문서화")
    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(3L, "선릉역", LocalDateTime.now(), LocalDateTime.now())
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        ExtractableResponse<Response> response = RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발 역 ID"),
                                parameterWithName("target").description("도착 역 ID")),
                        responseFields(
                                fieldWithPath("stations").description("역 목록"),
                                fieldWithPath("stations[].id").description("고유번호"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("stations[].createdDate").description("생성 시간"),
                                fieldWithPath("stations[].modifiedDate").description("최종 수정 시간"),
                                fieldWithPath("distance").description("전체 거리"))
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 3L)
                .when().get("/paths")
                .then().log().all().extract();


        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
