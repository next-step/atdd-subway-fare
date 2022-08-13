package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int shortestDistance;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public void setShortestDistance(int shortestDistance) {
        this.shortestDistance = shortestDistance;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() { return sections.totalDuration(); }

    public int getLineSurChage(){
        return sections.getMaxSurChage();
    }

}
