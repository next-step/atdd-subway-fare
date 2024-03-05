package nextstep.subway.domain.chain;

public class FareHandlerFactory {
    private final FareHandler fareHandler;

    public FareHandlerFactory() {
        BasicFareHandler basicFareHandler = new BasicFareHandler();
        Over10kmFareHandler over10kmFareHandler = new Over10kmFareHandler();
        Over50kmFareHandler over50kmFareHandler = new Over50kmFareHandler();

        basicFareHandler.setNextHandler(over10kmFareHandler);
        over10kmFareHandler.setNextHandler(over50kmFareHandler);

        this.fareHandler = basicFareHandler;
    }

    public long calculateFare(long distance) {
        return fareHandler.calculate(distance);
    }
}
