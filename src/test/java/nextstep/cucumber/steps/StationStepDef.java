package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.StationSteps.지하철역_목록_조회;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public StationStepDef() {

        When("지하철역을 생성하면", () -> {
            context.response = 지하철역_생성_요청("강남역");
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =지하철역_목록_조회().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

}
