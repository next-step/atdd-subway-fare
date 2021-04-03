package nextstep.subway.path.domain.fare;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private int fare;

    public Fare(int distance) {
        if(distance < 0) {
            throw new RuntimeException("요금은 양수입니다.");
        }
        calculate(distance);
    }

    private void calculate(int distance) {
        Distance10FareChain chain10 = new Distance10FareChain();
        Distance50FareChain chain50 = new Distance50FareChain();
        chain10.setFareChain(chain50);

        this.fare = chain10.calculate(distance, DEFAULT_FARE); ;
    }

    private int calculateOverFare(int distance, int offset) {
        return (int) ((Math.ceil((distance - 1) / offset) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
