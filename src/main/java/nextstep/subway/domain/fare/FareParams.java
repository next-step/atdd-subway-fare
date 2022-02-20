package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public class FareParams {

    private final int distance;
    private final int extraCharge;
    private final int age;

    private FareParams(int distance, int extraCharge, int age) {
        this.distance = distance;
        this.extraCharge = extraCharge;
        this.age = age;
    }

    public static FareParams of(int distance, int extraCharge, int age) {
        return new FareParams(distance, extraCharge, age);
    }

    public static FareParams of(Path path, int age) {
        return new FareParams(path.extractDistance(), path.extraCharge(), age);
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public int getAge() {
        return age;
    }
}
