package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.domain.fare.FareRule;
import support.ticket.TicketType;

import java.util.List;

@Getter
public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int extractFare(TicketType ticketType) {
        int distance = extractDistance();
        FareRule fareRule = FareRule.of(distance);

        return ticketType.applyFareDiscount(fareRule.getFare(distance) + sections.getSurcharge());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
