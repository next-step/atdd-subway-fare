package nextstep.subway.fixture;

import nextstep.subway.controller.dto.LineCreateRequest;
import nextstep.subway.domain.Line;

public enum LineFixture {
    SHINBUNDANG_LINE("신분당선", "bg-red-600", 1L, 2L, 10L, 60),
    BUNDANG_LINE("분당선", "bg-green-600", 1L, 2L, 10L, 60),
    ONE_LINE("일호선", "bg-yellow-600", 1L, 2L, 10L, 60),
    TWO_LINE("이호선", "bg-blue-600", 1L, 2L, 10L, 60),
    THREE_LINE("삼호선", "bg-black-600", 1L, 2L, 10L, 60);

    private final String lineName;
    private final String lineColor;
    private final Long startStationId;
    private final Long endStationId;
    private final Long distance;
    private final int duration;

    LineFixture(String lineName, String lineColor, Long startStationId, Long endStationId, Long distance, int duration) {
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public LineCreateRequest toCreateRequest(Long startStationId, Long endStationId) {
        return new LineCreateRequest(lineName, lineColor, startStationId, endStationId, distance);
    }

    public Line toLine(long id) {
        return new Line(id, lineName, lineColor);
    }
}
