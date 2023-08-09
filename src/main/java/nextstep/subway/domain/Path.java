package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.farechain.DistanceOverFare;
import nextstep.subway.domain.farechain.LineOverFare;
import nextstep.subway.domain.farechain.OverFarePolicyHandler;
import nextstep.subway.domain.farechain.OverFarePolicyHandlerImpl;

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

    public List<Line> getLines() {
        return sections.getSections().stream().map(Section::getLine).collect(Collectors.toList());
    }

    public int getFare() {

        OverFarePolicyHandler chain = new OverFarePolicyHandlerImpl(getHandlerChain());
        Fare fare = new Fare(chain);

        return fare.charge(this);
    }

    private OverFarePolicyHandler getHandlerChain() {
        return new DistanceOverFare(new LineOverFare(null));
    }
}
