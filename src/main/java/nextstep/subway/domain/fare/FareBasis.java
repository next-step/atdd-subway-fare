package nextstep.subway.domain.fare;

public class FareBasis {
    private int distance;
    private int age;
    private int lineCharge;

    public FareBasis(int distance, int age, int lineCharge) {
        this.distance = distance;
        this.age = age;
        this.lineCharge = lineCharge;
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public int getLineCharge() {
        return lineCharge;
    }
}
