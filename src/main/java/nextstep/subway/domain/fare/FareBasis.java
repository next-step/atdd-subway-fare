package nextstep.subway.domain.fare;

public class FareBasis {
    private int distance;
    private int age;
    private int lineFare;

    public FareBasis(int distance, int age, int lineFare) {
        this.distance = distance;
        this.age = age;
        this.lineFare = lineFare;
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public int getLineFare() {
        return lineFare;
    }
}
