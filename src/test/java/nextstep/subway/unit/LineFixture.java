package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class LineFixture {
    private static final BigDecimal DEFAULT_ADDITIONAL_FARE = BigDecimal.ZERO;

    public Line createLine(String name, String color) {
        return createLine(name, color, DEFAULT_ADDITIONAL_FARE);
    }

    public Line createLine(String name, String color, BigDecimal additionalFare) {
        return new Line(name, color, additionalFare);
    }

    public Station createStation(String name) {
        return new Station(name);
    }

    public Station createStation(long id, String name) {
        Station station = createStation(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
