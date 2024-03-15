package nextstep.core.subway.path.application.dto;

import nextstep.core.subway.favorite.application.dto.FavoriteRequest;

public class PathRequest {

    public final Long departureStationId;

    public final Long arrivalStationId;

    public String pathFinderType;

    public String token;

    public PathRequest(Long departureStationId, Long arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public PathRequest(Long departureStationId, Long arrivalStationId, String pathFinderType) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.pathFinderType = pathFinderType;
    }

    public PathRequest(Long departureStationId, Long arrivalStationId, String pathFinderType, String token) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.pathFinderType = pathFinderType;
        this.token = token;
    }

    public static PathRequest toRequest(FavoriteRequest request) {
        return new PathRequest(request.getSource(), request.getTarget());
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

    public String getToken() {
        return token;
    }
}
