package nextstep.subway.maps.boarding.domain;

import nextstep.subway.maps.line.domain.Fare;
import org.springframework.lang.NonNull;

public class FareCalculationContext {

    @NonNull
    private final Boarding boarding;

    private Fare previousResult;

    public FareCalculationContext(@NonNull Boarding boarding) {
        this.boarding = boarding;
    }

    public int getBoardingDistance() {
        return boarding.getBoardingDistance();
    }

    public void setCalculationResult(int result) {
        this.previousResult = new Fare(result);
    }

    public Fare previousResult() {
        return previousResult;
    }

    public Fare getMaximumExtraFareOnBoarding() {
        return boarding.getMaximumExtraFare();
    }
}
