package nextstep.cucumber.steps.line;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.steps.station.StationSteps;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStepDef implements En {
    @Autowired
    DatabaseCleanup databaseCleanup;
    ExtractableResponse<Response> response;
    Long upstationId = 1L;
    Long downstationId = 2L;

    public LineStepDef() {
        Before(() -> {
            databaseCleanup.execute();
        });

        Given("지하철역 두 개를 생성하고", () -> {
            StationSteps.역_생성_요청("방화역");
            StationSteps.역_생성_요청("마천역");
        });

        When("지하철 노선을 생성하면", () -> {
            response = LineSteps.노선_생성_요청("5호선", upstationId, downstationId, 5, 5);
        });

        Then("지하철 노선이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다", () -> {
            List<String> lineNames = LineSteps.노선_목록_조회().jsonPath().getList("name", String.class);
            assertThat(lineNames).containsAnyOf("5호선");
        });

    }
}