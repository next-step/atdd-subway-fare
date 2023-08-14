package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    private final Price price;

    public Path(Sections sections, Price price) {
        this.sections = sections;
        this.price = price;
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

    public int getPrice() {
        return price.getPrice();
    }
}
