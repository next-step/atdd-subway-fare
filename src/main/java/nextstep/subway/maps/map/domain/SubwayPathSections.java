package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubwayPathSections {
    private final List<SubwayPathSection> subwayPathSections = new ArrayList<>();

    private void add(SubwayPathSection section) {
        this.subwayPathSections.add(section);
    }

    public List<SubwayPathSection> getSubwayPathSections() {
        return this.subwayPathSections;
    }

    public Long getSourceStationId() {
        if (this.subwayPathSections.isEmpty()) {
            throw new RuntimeException("sections are empty");
        }
        SubwayPathSection firstSection = getFirstSection();
        return firstSection.getFirstStationId();
    }

    public Long getTargetStationId() {
        if (this.subwayPathSections.isEmpty()) {
            throw new RuntimeException("sections are empty");
        }
        SubwayPathSection lastSection = getLastSection();
        return lastSection.getLastStationId();
    }

    private SubwayPathSection getFirstSection() {
        return this.subwayPathSections.get(0);
    }

    private SubwayPathSection getLastSection() {
        int size = this.subwayPathSections.size();
        return this.subwayPathSections.get(size - 1);
    }

    public static SubwayPathSections from(List<LineStationEdge> lineStationEdges) {
        SubwayPathSections subwayPathSections = new SubwayPathSections();
        List<Pair<Line, List<LineStationEdge>>> splitBySection = splitBySection(lineStationEdges);

        for (Pair<Line, List<LineStationEdge>> sectionPair : splitBySection) {
            SubwayPathSection section = SubwayPathSection.createSubwayPathSection(sectionPair);
            subwayPathSections.add(section);
        }

        return subwayPathSections;
    }

    private static List<Pair<Line, List<LineStationEdge>>> splitBySection(List<LineStationEdge> lineStationEdges) {
        List<Pair<Line, List<LineStationEdge>>> splitBySection = new ArrayList<>();
        Line tempLine = lineStationEdges.stream().findFirst().orElseThrow(RuntimeException::new).getLine();

        List<LineStationEdge> tempList = new ArrayList<>();
        Pair<Line, List<LineStationEdge>> pair = Pair.of(tempLine, tempList);
        splitBySection.add(pair);

        for (LineStationEdge lineStationEdge : lineStationEdges) {
            Line line = lineStationEdge.getLine();
            if (!Objects.equals(tempLine, line)) {
                tempLine = line;
                tempList = new ArrayList<>();
                pair = Pair.of(line, tempList);
                splitBySection.add(pair);
            }

            pair.getSecond().add(lineStationEdge);
        }
        return splitBySection;
    }
}
