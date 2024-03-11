package nextstep.subway.line.section.dto;


import nextstep.subway.station.application.dto.StationResponse;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;
    private Long duration;

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation, Long distance, Long duration) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "SectionResponse{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                ", duration=" + duration +
                '}';
    }
}
