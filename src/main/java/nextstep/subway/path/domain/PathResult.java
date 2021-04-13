package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.path.domain.fare.FareByAddFare;
import nextstep.subway.path.domain.fare.FareDiscountByAge;
import nextstep.subway.path.domain.fare.FareByDistance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public class PathResult {

    private final Sections sections;
    private final Stations stations;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }

    public int getTotalFare(int age) {
        Fare fareByDistance = new FareByDistance(getTotalDistance());
        Fare fareByAddFare = new FareByAddFare(sections);
        int totalFare =  1250 + fareByDistance.calculate() + fareByAddFare.calculate();

        Fare fareDiscountByAge = new FareDiscountByAge(totalFare, age);
        int calculate = fareDiscountByAge.calculate();
        return totalFare - calculate;
    }
}
