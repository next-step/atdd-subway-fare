package nextstep.subway.path.domain.fare;

public class Fare {

    private int fare;

    public Fare(int distance, int age) {
        check(distance, age);
        FareCalculator fareCalculator = new FareCalculator();
        this.fare = fareCalculator.calculate(distance, age);
    }

    private void check(int distance, int age) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수이어야 합니다");
        }
        if(age < 0) {
            throw new RuntimeException("나이는 양수이어야 합니다");
        }
    }

    public int getFare() {
        return fare;
    }
}
