package nextstep.subway.domain.path.fee;

import java.util.List;

public class AdditionalFeeHandler extends CalculateHandler {

    public AdditionalFeeHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public void handle(FeeInfo pathInfo) {
        final List<Integer> fees = pathInfo.additionalFees();
        final int maxFee = fees.stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);

        super.fare = new Fare(maxFee);
        super.handle(pathInfo);
    }
}
