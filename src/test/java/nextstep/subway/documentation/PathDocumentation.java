package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.acceptance.MemberSteps;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
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

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    private static final String PATHS_PATH = "/paths?source={source}&target={target}&type={type}";
    private static final String DISTANCE = "distance";
    private static final String DURATION = "duration";

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private PathService pathService;

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

        lineService.addSection(이호선.getId(), new SectionRequest(교대역.getId(), 강남역.getId(), 1, 1));
        lineService.addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 2, 2));
        lineService.addSection(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3, 3));
        lineService.addSection(삼호선.getId(), new SectionRequest(남부터미널역.getId(), 양재역.getId(), 4, 4));
    }

    @Test
    @DisplayName("최단 거리 조회")
    void pathDistance() {
        requestSpecificationGiven()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역.getId())
                .queryParam("target", 양재역.getId())
                .queryParam("type", DISTANCE)
                .when().get(PATHS_PATH, 교대역.getId(), 양재역.getId(), DISTANCE)
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("최소 시간 조회")
    void pathDuration() {
        requestSpecificationGiven()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역.getId())
                .queryParam("target", 양재역.getId())
                .queryParam("type", DURATION)
                .when().get(PATHS_PATH, 교대역.getId(), 양재역.getId(), DURATION)
                .then().log().all()
                .extract();
    }

    private RequestSpecification requestSpecificationGiven() {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type")
                                        .description("엣지 가중치 전략 (distance, duration)")
                        ),
                        responseFields(
                                fieldWithPath("stations[].id").description("역 id"),
                                fieldWithPath("stations[].name").description("역 이름"),
                                fieldWithPath("distance").description("총 거리"),
                                fieldWithPath("duration").description("총 시간")
                        )
                ));
    }

}
