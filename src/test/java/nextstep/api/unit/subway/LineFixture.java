package nextstep.api.unit.subway;

import nextstep.api.subway.domain.line.Line;
import nextstep.api.subway.domain.line.Section;
import nextstep.api.subway.domain.station.Station;

public class LineFixture {

    public static final String DEFAULT_LINE_NAME = "신분당선";
    public static final String DEFAULT_LINE_COLOR = "bg-red-600";
    public static final int DEFAULT_LINE_DISTANCE = 10;
    public static final int DEFAULT_LINE_DURATION = 10;

    public static Line makeLine(final Station upStation, final Station firstDownStation, Station... downStations) {
        final var line = new Line(DEFAULT_LINE_NAME, DEFAULT_LINE_COLOR,
                new Section(upStation, firstDownStation, DEFAULT_LINE_DISTANCE, DEFAULT_LINE_DURATION));

        if (downStations.length == 0) {
            return line;
        }

        appendSection(line, firstDownStation, downStations[0], DEFAULT_LINE_DISTANCE, DEFAULT_LINE_DURATION);

        if (downStations.length > 1) {
            for (int i = 0; i < downStations.length - 1; i++) {
                appendSection(line, downStations[i], downStations[i + 1], DEFAULT_LINE_DISTANCE, DEFAULT_LINE_DURATION);
            }
        }

        return line;
    }

    public static Line makeLine(final Station upStation, final Station downStation, final int distance,
                                final int duration) {
        return new Line(DEFAULT_LINE_NAME, DEFAULT_LINE_COLOR,
                new Section(upStation, downStation, distance, duration));
    }

    public static void appendSection(final Line line, final Station upStation, final Station downStation,
                                     final int distance, final int duration) {
        line.appendSection(new Section(upStation, downStation, distance, duration));
    }
}
