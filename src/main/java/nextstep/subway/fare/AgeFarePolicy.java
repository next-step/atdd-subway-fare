package nextstep.subway.fare;

public abstract class AgeFarePolicy {
    private final AgeFarePolicy next;

    public AgeFarePolicy(AgeFarePolicy next) {
        this.next = next;
    }

    public final int calculate(int age, int fare) {
        if (isInAge(age)) {
            return calculateFare(fare);
        }
        if (next != null) {
            return next.calculate(age, fare);
        }
        return fare;
    }

    public int calculateFare(int fare, double ratio) {
        return (int) ((fare - 350) * ratio) + 350;
    }

    protected abstract int calculateFare(int fare);

    protected abstract boolean isInAge(int age);

}
