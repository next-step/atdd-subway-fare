package nextstep.cucumber.steps;

import nextstep.line.domain.Color;
import nextstep.line.ui.LineRequest;
import nextstep.line.ui.SectionRequest;
import nextstep.line.ui.SectionResponse;
import nextstep.subway.fixture.LineSteps;
import nextstep.subway.fixture.SectionSteps;
import nextstep.subway.fixture.StationSteps;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.*;

@Component
public class PathAcceptanceContext {
    public Map<String, Long> stationStore = new HashMap<>();
    public Map<String, Long> lineStore = new HashMap<>();
    List<String> 이호선_역;
    String 이호선 = "이호선";

    List<String> 오호선_역;
    String 오호선 = "오호선";

    public void setUpLine() {
        String start = 이호선_역.get(0);
        String end = 이호선_역.get(이호선_역.size() - 1);
        long lineId = LineSteps.createLine(new LineRequest(이호선, Color.GREEN, stationStore.get(start), stationStore.get(end), 50, 10)).getId();
        lineStore.put(이호선, lineId);

        start = 오호선_역.get(0);
        end = 오호선_역.get(오호선_역.size() - 1);
        lineId = LineSteps.createLine(new LineRequest(오호선, Color.GREEN, stationStore.get(start), stationStore.get(end), 90, 100)).getId();
        lineStore.put(오호선, lineId);
    }

    public void setUpStation(List<Map<String, String>> rows) {
        Map<String, List<String>> staionMap = rows.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())));
        이호선_역 = staionMap.get("이호선");
        오호선_역 = staionMap.get("오호선");

        for (String station : 이호선_역) {
            if(!stationStore.containsKey(station)){
                Long id = StationSteps.createStation(station).getId();
                stationStore.put(station, id);
            }
        }

        for (String station : 오호선_역) {
            if (!stationStore.containsKey(station)) {
                Long id = StationSteps.createStation(station).getId();
                stationStore.put(station, id);
            }
        }
    }

    public void setUpSection(Map<String, Integer> durationByLine) {
        for (int i = 0; i < 이호선_역.size() - 2; i++) {
            int next = i + 1;
            SectionSteps.라인에_구간을_추가한다(lineStore.get(이호선), new SectionRequest(
                    stationStore.get(이호선_역.get(i)),
                    stationStore.get(이호선_역.get(next)),
                    5,
                    durationByLine.get("이호선")
            ));
        }

        for (int i = 0; i < 오호선_역.size() - 2; i++) {
            int next = i + 1;
            SectionSteps.라인에_구간을_추가한다(lineStore.get(오호선), new SectionRequest(
                    stationStore.get(오호선_역.get(i)),
                    stationStore.get(오호선_역.get(next)),
                    (5 + 2),
                    durationByLine.get("오호선")
            ));
        }
    }

    public void cleanUp() {
        stationStore = new HashMap<>();
        lineStore = new HashMap<>();
        이호선_역 = new ArrayList<>();
        오호선_역 = new ArrayList<>();
    }
}
