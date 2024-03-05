package nextstep.cucumber.steps;

import nextstep.line.ui.LineRequest;
import nextstep.line.ui.SectionRequest;
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
    List<String> 오호선_역;
    List<String> 분당선_역;
    String 이호선 = "이호선";
    String 오호선 = "오호선";

    public void setUpLine(List<LineRequest> lineRequests){
        for (LineRequest lineRequest : lineRequests) {
            Long id = LineSteps.createLine(lineRequest).getId();
            lineStore.put(lineRequest.getName(), id);
        }
    }

    public void setUpStation(List<Map<String, String>> rows) {
        Map<String, List<String>> staionMap = rows.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())));

        이호선_역.addAll(staionMap.get("이호선"));
        오호선_역.addAll(staionMap.get("오호선"));
        분당선_역.addAll(staionMap.get("분당선"));
        addStationStore(이호선_역);
        addStationStore(오호선_역);
        addStationStore(분당선_역);
    }

    public void setUpSection(Map<String, PathStepDef.SectionInfo> sectionInfos) {
        createSection(오호선_역, sectionInfos, "오호선");
        createSection(분당선_역, sectionInfos, "분당선");
        createSection(이호선_역, sectionInfos, "이호선");
    }

    private void createSection(List<String> stationNames,
                               Map<String, PathStepDef.SectionInfo> sectionInfos,
                               String lineName) {
        for (int i = 0; i < stationNames.size() - 2; i++) {
            int next = i + 1;
            PathStepDef.SectionInfo info = sectionInfos.get(lineName);
            SectionSteps.라인에_구간을_추가한다(lineStore.get(lineName), new SectionRequest(
                    stationStore.get(stationNames.get(i)),
                    stationStore.get(stationNames.get(next)),
                    info.distance,
                    info.duration
            ));
        }
    }


    private void addStationStore(List<String> stationNames) {
        for (String station : stationNames) {
            if (!stationStore.containsKey(station)) {
                Long id = StationSteps.createStation(station).getId();
                stationStore.put(station, id);
            }
        }
    }

    public void cleanUp() {
        stationStore = new HashMap<>();
        lineStore = new HashMap<>();
        이호선_역 = new ArrayList<>();
        오호선_역 = new ArrayList<>();
        분당선_역 = new ArrayList<>();
    }

}
