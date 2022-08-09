package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractFare() {
        // distance가 10 이내면 1250
        // distance가 10초과∼50미만 시에 5마다 100 추가
        // distance가 50초과 시 8km마다 100 추가

        int distance = sections.totalDistance();
        int fare = 1250;


        if (distance > 50) {
            fare += ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100);
            distance = 50;
        }

        if (distance > 10) {
            fare += ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100);
            distance = 10;
        }

        return fare;
    }
}
