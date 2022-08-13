package nextstep.subway.domain;

import nextstep.subway.domain.policy.FareManager;

import java.util.List;

public class Fare {

    private Sections sections;

    public Fare(List<Section> sections) {
        this(new Sections(sections));
    }

    public Fare(Sections sections) {
        this.sections = sections;
    }

    public int extractFare() {
        int totalDistance = sections.totalDistance();
        return FareManager.fare(totalDistance);
    }
}
