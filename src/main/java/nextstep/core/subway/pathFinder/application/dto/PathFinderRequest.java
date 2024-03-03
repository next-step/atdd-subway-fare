package nextstep.core.subway.pathFinder.application.dto;

public class PathFinderRequest {

    public final Long departureStationId;

    public final Long arrivalStationId;

    public String pathFinderType;

    public PathFinderRequest(Long departureStationId, Long arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public PathFinderRequest(Long departureStationId, Long arrivalStationId, String pathFinderType) {
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
