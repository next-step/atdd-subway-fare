package nextstep.subway.maps.boarding.domain;

import org.springframework.lang.NonNull;

public class FareCalculationContext {

    @NonNull
    private final Boarding boarding;

    private Integer previousResult;

    public FareCalculationContext(@NonNull Boarding boarding) {
        this.boarding = boarding;
    }

    public int getBoardingDistance() {
        return boarding.getBoardingDistance();
    }

    public void setCalculationResult(int result) {
        this.previousResult = result;
    }

    public int previousResult() {
        return previousResult;
    }
}
