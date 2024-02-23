package nextstep.line.application.dto;

import nextstep.line.domain.Section;
import nextstep.station.application.dto.StationResponse;

public class SectionResponse {
    private Long id;

    private StationResponse upStation;

    private StationResponse downStation;

    private int distance;
    private int duration;

    public SectionResponse() {
    }

    public SectionResponse(final Long id, final StationResponse upStation, final StationResponse downStation, final int distance, final int duration) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public static SectionResponse from(final Section savedSection) {
        return new SectionResponse(
                savedSection.getId(),
                StationResponse.from(savedSection.getUpStation()),
                StationResponse.from(savedSection.getDownStation()),
                savedSection.getDistance(),
                savedSection.getDuration()
        );
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

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
