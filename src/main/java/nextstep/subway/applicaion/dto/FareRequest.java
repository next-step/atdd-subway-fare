package nextstep.subway.applicaion.dto;

public class FareRequest {

    private final int age;
    private final int fare;

    public FareRequest(int age, int fare) {
        this.age = age;
        this.fare = fare;
    }

    public static FareRequest valueOf(int age) {
        return new FareRequest(age, 0);
    }

    public int getAge() {
        return age;
    }

    public int getFare() {
        return fare;
    }
}
