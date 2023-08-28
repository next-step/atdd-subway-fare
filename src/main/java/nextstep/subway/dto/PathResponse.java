package nextstep.subway.dto;

import java.math.BigInteger;
import java.util.List;

public class PathResponse {

    public List<StationResponse> getStations() {
        return stations;
    }

    public BigInteger getDistance() {
        return distance;
    }

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, BigInteger distance) {
        this.stations = stations;
        this.distance = distance;
    }

    private List<StationResponse> stations;
    private BigInteger distance;
}
