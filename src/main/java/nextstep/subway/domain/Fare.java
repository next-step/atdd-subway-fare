package nextstep.subway.domain;

import nextstep.subway.domain.policy.FareManager;

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

    public Fare calculate(int distance) {
        if (done) {
            throw new IllegalStateException();
        }
        value += FareManager.fare(distance);
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
