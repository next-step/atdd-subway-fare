package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class SubwayPathSections {
    private final List<SubwayPathSection> subwayPathSections = new ArrayList<>();

    public SubwayPathSections() {

    }

    public static SubwayPathSections from(List<LineStationEdge> lineStationEdges) {
        Line tempLine = null;
        SubwayPathSection subwayPathSection = null;
        SubwayPathSections subwayPathSections = new SubwayPathSections();

        for (LineStationEdge lineStationEdge : lineStationEdges) {
            if (!lineStationEdge.getLine().equals(tempLine)) {
                tempLine = lineStationEdge.getLine();
                subwayPathSection = new SubwayPathSection(tempLine);
                subwayPathSections.add(subwayPathSection);
            }

            subwayPathSection.addLineStationEdge(lineStationEdge);
        }

        return subwayPathSections;
    }

    public int countLines() {
        return subwayPathSections.size();
    }

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
}
