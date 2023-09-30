package nextstep.subway.documentation.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.documentation.Documentation;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.util.List;

import static nextstep.subway.documentation.path.PathDocumentationSteps.경로_조회_요청_문서화;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;
    @Autowired
    private StationRepository stationRepository;
    private Station 강남역;
    private Station 양재역;
    private Long 강남역_식별값;
    private Long 양재역_식별값;

    @BeforeEach
    void setUpPathDocumentation() {
        강남역 = stationRepository.save(new Station("양재역"));
        양재역 = stationRepository.save(new Station("강남역"));
        강남역_식별값 = 강남역.getId();
        양재역_식별값 = 양재역.getId();
    }

    @DisplayName("지하철 경로 조회 API 문서화")
    @Test
    void path() {
        // given
        PathResponse mockResponse = new PathResponse(List.of(
                new StationResponse(강남역),
                new StationResponse(양재역)),
                10);
        Mockito.when(pathService.findPath(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(mockResponse);

        // when
        ExtractableResponse<Response> response = 경로_조회_요청_문서화(getPathSpec(), 강남역_식별값, 양재역_식별값);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private RequestSpecification getPathSpec() {
        return getSpec(
                "path",
                getPathsRequestField(),
                getPathsResponseField());
    }

    private RequestParametersSnippet getPathsRequestField() {
        return requestParameters(
                parameterWithName("source").description("출발 역"),
                parameterWithName("target").description("종점 역"));
    }

    private ResponseFieldsSnippet getPathsResponseField() {
        return responseFields(
                fieldWithPath("stations[].id").description("지하철 역 id"),
                fieldWithPath("stations[].name").description("지하철 역 이름"),
                fieldWithPath("stations[].createdTime").description("지하철 역 생성 시간"),
                fieldWithPath("stations[].modifiedTime").description("지하철 역 변경 시간"),
                fieldWithPath("distance").description("지하철 구간 거리"));
    }
}