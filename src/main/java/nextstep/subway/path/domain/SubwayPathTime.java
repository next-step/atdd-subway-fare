package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubwayPathTime {

    private final PathResult pathResults;

    public SubwayPathTime(PathResult pathResults) {
        this.pathResults = pathResults;
    }

    public LocalDateTime getArriveTime(LocalDateTime dateTime) {
        Map<Line, List<Section>> linesMap = pathResults.getSections()
                .stream()
                .collect(Collectors.groupingBy(Section::getLine));

        LocalDateTime arriveTime = dateTime;
        for (Map.Entry<Line, List<Section>> entry: linesMap.entrySet()) {
            Line line = entry.getKey();
            List<Section> sections = entry.getValue();
            Station source = sections.get(0).getUpStation();
            Station target = sections.get(sections.size() - 1).getDownStation();

            SubwayTime subwayTime = new SubwayTime(line);
            arriveTime = subwayTime.getArriveTime(source, target, arriveTime);
        }

        return arriveTime;
    }
}
