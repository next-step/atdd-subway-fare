package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int shortDistance;

    public Path(Sections sections, int shortDistance) {
        this.sections = sections;
        this.shortDistance = shortDistance;
    }

    public Sections getSections() {
        return sections;
    }

    public int getShortDistance() {
        return shortDistance;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    /*
        public int extractFare(){
            return SubwayFare.calculateFare(shortDistance);
        }
    */
    public int extractExtraCharge() {
        return sections.getMaxExtraCharge();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }


}
