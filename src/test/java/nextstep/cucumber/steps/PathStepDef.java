package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.line.domain.Color;
import nextstep.line.presentation.LineRequest;
import nextstep.path.domain.dto.StationDto;
import nextstep.path.presentation.PathsResponse;
import nextstep.subway.fixture.LineSteps;
import nextstep.subway.fixture.PathSteps;
import nextstep.subway.fixture.StationSteps;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    PathsResponse response;
    long 성수역;
    long 강변역;
    long 이호선;

    public PathStepDef() {
        Given("{string} 노선을 생성하고", (String lineName) -> {
            성수역 = StationSteps.createStation("성수역").getId();
            강변역 = StationSteps.createStation("강변역").getId();
            이호선 = LineSteps.노선_생성(new LineRequest(lineName, Color.GREEN, 성수역, 강변역, 10)).getId();
        });
        When("노선 출발과 도착역의 경로를 요청하면", () -> {
            response = PathSteps.getPath(성수역, 강변역);
        });
        Then("출발역으로부터 도착역까지의 경로에 있는 역 목록이 조회된다", () -> {
            List<Long> actual = response.getStationDtoList().stream().map(StationDto::getId).collect(Collectors.toList());
            assertThat(actual).containsExactly(성수역, 강변역);
        });
    }

}
