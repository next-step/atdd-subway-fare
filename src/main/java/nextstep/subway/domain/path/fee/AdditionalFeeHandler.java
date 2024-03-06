package nextstep.subway.domain.path.fee;

import java.util.List;

public class AdditionalFeeHandler extends CalculateHandler {

    public AdditionalFeeHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public Fare handle(FeeInfo pathInfo, Fare beforeFare) {
        final List<Integer> fees = pathInfo.additionalFees();
        final int maxFee = fees.stream()
                .mapToInt(x -> x)
                .max()
                .orElse(0);

        Fare fare = new Fare(maxFee);
        return super.handle(pathInfo, beforeFare.add(fare));
    }
}
