package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java8.En;
import nextstep.path.domain.dto.StationDto;
import nextstep.path.ui.PathType;
import nextstep.path.ui.PathsResponse;
import nextstep.subway.fixture.PathSteps;
import org.apache.groovy.util.Maps;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    PathsResponse response;
    @Autowired
    private PathAcceptanceContext cxt;


    public PathStepDef() {
        Given("지하철역이 등록되어있음", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            cxt.setUpStation(maps);
        });
        And("지하철 노선이 등록되어있음", () -> {
            cxt.setUpLine();
        });
        And("지하철 노선에 지하철역이 등록되어있음", (DataTable table) -> {
            String 이호선_구간별_duration = table.cell(1, 2);
            String 오호선_구간별_duration = table.cell(2, 2);
            Map<String, Integer> durationByLine = Maps.of(
                    "이호선", Integer.parseInt(이호선_구간별_duration),
                    "오호선", Integer.parseInt(오호선_구간별_duration));
            cxt.setUpSection(durationByLine);
        });
        When("{string}에서 {string}까지의 {string} 기준으로 경로 조회를 요청", (String start, String end, String pathType) -> {
            response = PathSteps.getPath(cxt.stationStore.get(start), cxt.stationStore.get(end), convertPathType(pathType));
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
        And("지하철 이용 요금도 함께 응답함", () -> {
            Assertions.assertThat(response.getFare()).isEqualTo(1450);
        });
    }

    private static PathType convertPathType(String pathType) {
        if ("DURATION".equals(pathType)) {
            return PathType.DURATION;
        } else if ("DISTANCE".equals(pathType)) {
            return PathType.DISTANCE;
        }
        throw new IllegalStateException("PathType enum doesn't have : " + pathType);
    }

}
