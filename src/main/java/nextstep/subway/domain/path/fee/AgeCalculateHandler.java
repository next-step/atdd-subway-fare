package nextstep.subway.domain.path.fee;

public class AgeCalculateHandler extends CalculateHandler {

    public AgeCalculateHandler(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    public Fare handle(FeeInfo pathInfo, Fare beforeFare) {
        final AgeType ageType = pathInfo.ageType();

        return super.handle(pathInfo, ageType.calculate(beforeFare));
    }
}
