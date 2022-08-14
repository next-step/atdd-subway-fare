package nextstep.subway.documentation;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.MemberSteps;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

class PathDocumentation extends Documentation {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationRepository stationRepository;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;
    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    String 관리자;

    @BeforeEach
    void setUp() {
        관리자 = MemberSteps.로그인_되어_있음("admin@email.com", "password");
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        이호선 = lineRepository.save(new Line("2호선", "green"));
        신분당선 = lineRepository.save(new Line("신분당선", "red"));
        삼호선 = lineRepository.save(new Line("3호선", "orange"));

        lineService.addSection(이호선.getId(), new SectionRequest(교대역.getId(), 강남역.getId(), 2, 3));
        lineService.addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3, 5));
        lineService.addSection(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 10, 2));
        lineService.addSection(삼호선.getId(), new SectionRequest(남부터미널역.getId(), 양재역.getId(), 10, 2));
    }

    @Test
    @DisplayName("최단 거리 조회")
    void pathDistance() {
        //then
        최단_거리를_조회한다();
    }

    private ExtractableResponse<Response> 최단_거리를_조회한다() {
        return RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                요청_파라미터_생성(),
                응답_객체_생성()
            ))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={source}&target={target}", 교대역.getId(), 양재역.getId())
            .then().log().all()
            .extract();
    }

    @Test
    @DisplayName("최소 시간 조회")
    void pathDuration() {
        //then
        최소_시간으로_조회한다();
    }

    private void 최소_시간으로_조회한다() {
        RestAssured
            .given(spec)
            .filter(document("path-time",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                요청_파라미터_생성(),
                응답_객체_생성()
            ))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역.getId())
            .queryParam("target", 양재역.getId())
            .when().get("/paths/time?source={source}&target={target}", 교대역.getId(), 양재역.getId())
            .then().log().all()
            .extract();
    }

    private RequestParametersSnippet 요청_파라미터_생성() {
        return requestParameters(
            parameterWithName("source").description("출발역"),
            parameterWithName("target").description("도착역")
        );
    }

    private ResponseFieldsSnippet 응답_객체_생성() {
        return responseFields(
            fieldWithPath("stations[].id").description("역 id"),
            fieldWithPath("stations[].name").description("역 이름"),
            fieldWithPath("price").description("기본 가격"),
            fieldWithPath("distance").description("총 거리"),
            fieldWithPath("duration").description("총 시간"),
            fieldWithPath("fare").description("총 요금")
        );
    }
}
