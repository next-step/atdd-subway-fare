package nextstep.subway.domain;

import nextstep.subway.domain.policy.FareManager;
import nextstep.subway.domain.policy.PathByFare;

public class Fare {

    private int value;
    private boolean done;

    private Fare(int value, boolean done) {
        this.value = value;
        this.done = done;
    }

    public static Fare chaining() {
        return new Fare(0, false);
    }

    public Fare calculate(PathByFare pathByFare) {
        if (done) {
            throw new IllegalStateException();
        }
        value += FareManager.fare(pathByFare);
        done();
        return this;
    }

    private void done() {
        done = true;
    }

    public int toInt() {
        if (isNotFinishCalculate()) {
            throw new IllegalStateException();
        }
        return value;
    }

    private boolean isNotFinishCalculate() {
        return !done;
    }
}
