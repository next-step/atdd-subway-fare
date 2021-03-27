package nextstep.subway.path.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        ArrayList<StationResponse> stationResponses = Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
        );

        PathResponse pathResponse = new PathResponse(stationResponses, 10, 10);
        given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        // when & then
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 ID"),
                                parameterWithName("target").description("도착역 ID"),
                                parameterWithName("type").description("조회 방법")
                        ),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 지하철역 목록"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("지하철 역 ID"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("지하철 역 이름"),
                                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("지하철 역 등록 날짜"),
                                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("지하철 역 최종 수정 날짜"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리 (km)"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간 (분)")
                        )))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all();
    }
}

