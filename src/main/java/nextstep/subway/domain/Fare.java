package nextstep.subway.domain;

public enum Fare {
    BASIC_FARE(1250),
    OVER_FARE(100);

    private final int amount;

    Fare(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
