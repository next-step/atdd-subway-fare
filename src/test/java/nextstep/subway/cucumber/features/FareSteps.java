package nextstep.subway.cucumber.features;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.세션_생성_파라미터_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.acceptance.LineSteps;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class FareSteps {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private ExtractableResponse<Response> response;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @Given("지하철역이 등록되어있음")
    public void registerStation() {
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
    }

    @And("지하철 노선이 등록되어있음")
    public void registerLine() {
        이호선 = LineSteps.지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        신분당선 = LineSteps.지하철_노선_생성_요청("신분당선", "red").jsonPath().getLong("id");
        삼호선 = LineSteps.지하철_노선_생성_요청("3호선", "orange").jsonPath().getLong("id");
    }

    @And("지하철 노선에 지하철역이 등록되어있음")
    public void registerStationToLine() {
        지하철_노선에_지하철_구간_생성_요청(이호선, 세션_생성_파라미터_생성(교대역, 강남역, 10, 10));
        지하철_노선에_지하철_구간_생성_요청(신분당선, 세션_생성_파라미터_생성(강남역, 양재역, 10, 5));
        지하철_노선에_지하철_구간_생성_요청(삼호선, 세션_생성_파라미터_생성(교대역, 남부터미널역, 2, 20));
        지하철_노선에_지하철_구간_생성_요청(삼호선, 세션_생성_파라미터_생성(남부터미널역, 양재역, 3, 2));
    }


    @When("출발역에서 도착역까지의 최단 거리 경로 조회를 요청")
    public void findShortestPath() {
        response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);
    }

    @Then("최단 거리 경로를 응답")
    public void sendShortestPathResponse() {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsAll(List.of(교대역, 남부터미널역, 양재역));
    }

    @And("총 거리와 소요 시간을 함께 응답함")
    public void provideDistanceAndTimeInfo() {
        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(22)
        );
    }

    @And("지하철 이용 요금도 함께 응답함")
    public void provideFare() {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(0);
    }
}
