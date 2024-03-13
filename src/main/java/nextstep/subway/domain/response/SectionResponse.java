package nextstep.subway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    Long id;
    Station upStation;
    Station downStation;
    int distance;
    int duration;

    public SectionResponse from(Section section) {
        this.id = section.getId();
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.distance = section.getDistance();
        this.duration = section.getDuration();

        return this;
    }
}

