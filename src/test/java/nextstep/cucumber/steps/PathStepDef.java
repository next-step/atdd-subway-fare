package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.path.domain.dto.StationDto;
import nextstep.path.ui.PathType;
import nextstep.path.ui.PathsResponse;
import nextstep.subway.fixture.PathSteps;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    PathsResponse response;
    @Autowired
    private PathAcceptanceContext cxt;

    public PathStepDef() {
        Given("지하철역이 등록되어있음", () -> {
            cxt.setUpStation();
        });
        And("지하철 노선이 등록되어있음", () -> {
            cxt.setUpLine();
        });
        And("지하철 노선에 지하철역이 등록되어있음", () -> {
            cxt.setUpSection();
        });
        When("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청", (String start, String end) -> {
            response = PathSteps.getPath(cxt.stationStore.get(start), cxt.stationStore.get(end), PathType.DURATION);
        });
        Then("최소 시간 기준 경로를 응답", () -> {
            List<StationDto> stationsOnPath = response.getStationDtoList();
            List<String> names = stationsOnPath.stream().map(StationDto::getName).collect(Collectors.toList());
            Assertions.assertThat(names).containsExactly(
                    "을지로4가", "동대문역사문화공원", "신당", "상왕십리", "왕십리");
        });
        And("총 거리와 소요 시간을 함께 응답함", () -> {
            int duration = response.getDuration();
            int distance = response.getDistance();
            Assertions.assertThat(duration).isEqualTo(4);
            Assertions.assertThat(distance).isEqualTo(20);
        });
    }

}
