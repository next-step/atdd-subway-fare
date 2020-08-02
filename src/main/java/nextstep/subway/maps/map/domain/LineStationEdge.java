package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.LineStation;
import org.jgrapht.graph.DefaultWeightedEdge;

public class LineStationEdge extends DefaultWeightedEdge {
    private final LineStation lineStation;
    private final Long lineId;
    private final Integer extraFare;

    public LineStationEdge(LineStation lineStation, Long lineId, Integer extraFare) {
        this.lineStation = lineStation;
        this.lineId = lineId;
        this.extraFare = extraFare;
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    @Override
    protected Object getSource() {
        return this.lineStation.getPreStationId();
    }

    @Override
    protected Object getTarget() {
        return this.lineStation.getStationId();
    }

    public Long extractTargetStationId(Long preStationId) {
        if (lineStation.getStationId().equals(preStationId)) {
            return lineStation.getPreStationId();
        } else if (lineStation.getPreStationId().equals(preStationId)) {
            return lineStation.getStationId();
        } else {
            throw new RuntimeException();
        }
    }
}
