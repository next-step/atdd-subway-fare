package nextstep.core.subway.path.application.dto;

public class PathRequest {

    public final Long departureStationId;

    public final Long arrivalStationId;

    public String pathFinderType;

    public PathRequest(Long departureStationId, Long arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public PathRequest(Long departureStationId, Long arrivalStationId, String pathFinderType) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.pathFinderType = pathFinderType;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getArrivalStationId() {
        return arrivalStationId;
    }

    public String getPathFinderType() {
        return pathFinderType;
    }
}
