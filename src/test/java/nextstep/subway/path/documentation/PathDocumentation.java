package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.time.LocalDateTime;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("경로찾기 문서")
public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("두 역의 최단 거리 경로를 조회한다")
    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now()))
                , 10, 10, 1250);

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class), any(LoginMember.class))).thenReturn(pathResponse);

        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("source").description("출발역 아이디"),
                parameterWithName("target").description("도착역 아이디"),
                parameterWithName("type").description("검색 기준"));

        ResponseFieldsSnippet responseFieldsSnippet = responseFields(
                fieldWithPath("stations.[].id").description("지하철역 아이디"),
                fieldWithPath("stations.[].name").description("지하철역 이름"),
                fieldWithPath("stations.[].createdDate").description("생성된 날짜"),
                fieldWithPath("stations.[].modifiedDate").description("마지막 수정된 날짜"),
                fieldWithPath("distance").description("총 이동 거리"),
                fieldWithPath("duration").description("총 이동 시간"),
                fieldWithPath("fare").description("요금"));

        RequestSpecification requestSpecification = RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFieldsSnippet,
                        requestParametersSnippet));

        // when
        두_역의_최단_거리_경로_조회_요청(requestSpecification,1L, 2L, "DISTANCE");
    }
}

