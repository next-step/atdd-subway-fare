package nextstep.subway.line.domain.entity;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;

@Getter
@Embeddable
@NoArgsConstructor
public class AdditionalFare {

    private Long additionalFare;

    public AdditionalFare(Long additionalFare) {
        if (additionalFare < 0) {
            throw new SubwayException(SubwayExceptionType.INVALID_FARE, additionalFare.toString());
        }

        this.additionalFare = additionalFare;
    }
}
