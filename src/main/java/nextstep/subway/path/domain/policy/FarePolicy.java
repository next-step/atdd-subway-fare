package nextstep.subway.path.domain.policy;

public interface FarePolicy {
    int PER_FIVE_KILLO = 5;
    int PER_EIGHT_KILLO = 8;

    int calculate();

    static int calculateOverFare(int distance, int perKillo) {
        return (int) ((Math.ceil((distance - 1) / perKillo) + 1) * 100);
    }
}
