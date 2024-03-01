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
        Given("지하철역이 등록되어있음", () -> {
            성수역 = StationSteps.createStation("성수역").getId();
            강변역 = StationSteps.createStation("강변역").getId();
            이호선 = LineSteps.노선_생성(new LineRequest("이호선", Color.GREEN, 성수역, 강변역, 10)).getId();
        });
        And("지하철 노선이 등록되어있음", () -> {

        });
        And("지하철 노선에 지하철역이 등록되어있음", () -> {

        });
        When("출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청", () -> {
            response = PathSteps.getPath(성수역, 강변역);
        });
        Then("최소 시간 기준 경로를 응답", () -> {
            List<Long> actual = response.getStationDtoList().stream().map(StationDto::getId).collect(Collectors.toList());
            assertThat(actual).containsExactly(성수역, 강변역);
        });
        And("총 거리와 소요 시간을 함께 응답함", () -> {

        });
    }

}
