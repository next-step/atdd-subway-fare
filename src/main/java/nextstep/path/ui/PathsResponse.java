package nextstep.path.ui;


import nextstep.path.domain.dto.PathsDto;
import nextstep.path.domain.dto.StationDto;

import java.util.List;
import java.util.stream.Collectors;

public class PathsResponse {

    private int distance;
    private int duration;
    private int fare;
    private List<StationDto> stationDtoList;

    public PathsResponse() {
    }


    public PathsResponse(int distance, int duration, int fare, List<StationDto> stationDtoList) {
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.stationDtoList = stationDtoList;
    }


    public int getDistance() {
        return distance;
    }

    public List<StationDto> getStationDtoList() {
        return stationDtoList;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }

    public static PathsResponse of(PathsDto pathsDto, int fare) {
        return new PathsResponse(
                pathsDto.getDistance(),
                pathsDto.getDuration(),
                fare,
                pathsDto.getPaths()
                .stream()
                .map(it -> new StationDto(it.getId(), it.getName()))
                .collect(Collectors.toList()));
    }
}
