package nextstep.subway.maps.boarding.domain;

import nextstep.subway.maps.line.domain.Fare;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.Member;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author hyeyoom
 */
public class Boarding {

    @Nullable
    private Member passenger;

    @NonNull
    private final SubwayPath subwayPath;

    public Boarding(@NonNull SubwayPath subwayPath) {
        this.subwayPath = subwayPath;
    }

    public Boarding(@Nullable Member passenger, @NonNull SubwayPath subwayPath) {
        this.passenger = passenger;
        this.subwayPath = subwayPath;
    }

    public int getBoardingDistance() {
        return subwayPath.calculateDistance();
    }

    public Fare getMaximumExtraFare() {
        final int amount = subwayPath.getMaximumExtraFare();
        return new Fare(amount);
    }
}
