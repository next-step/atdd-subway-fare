package nextstep.subway.applicaion.dto;

public class FareResponse {
    private final int amount;

    public FareResponse() {
        this(0);
    }

    public FareResponse(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
