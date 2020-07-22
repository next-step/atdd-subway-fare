package nextstep.subway.maps.line.dto;

import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.station.dto.StationResponse;

public class LineStationResponse {
    private StationResponse station;
    private Long preStationId;
    private Long lineId;
    private Integer distance;
    private Integer duration;

    public LineStationResponse() {
    }

    public LineStationResponse(StationResponse station, Long preStationId, Long lineId, Integer distance, Integer duration) {
        this.station = station;
        this.preStationId = preStationId;
        this.lineId = lineId;
        this.distance = distance;
        this.duration = duration;
    }

    public static LineStationResponse of(Long lineId, LineStation it, StationResponse station) {
        return new LineStationResponse(station, it.getPreStationId(), lineId, it.getDistance(), it.getDuration());
    }

    public StationResponse getStation() {
        return station;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}