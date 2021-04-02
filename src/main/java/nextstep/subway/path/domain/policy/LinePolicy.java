package nextstep.subway.path.domain.policy;

public class LinePolicy {

    public static final int ZERO_FARE = 0;

    public static FarePolicy getLinePolicy(int lineFare) {
        if (lineFare > ZERO_FARE) {
            return new LineFarePolicy(lineFare);
        }
        return new DefaultLinePolicy();
    }

    public static class LineFarePolicy implements FarePolicy {
        private final int lineFare;

        public LineFarePolicy(int lineFare) {
            this.lineFare = lineFare;
        }

        @Override
        public int fareCalculate(int fare) {
            return fare + lineFare;
        }
    }

    public static class DefaultLinePolicy implements FarePolicy {

        @Override
        public int fareCalculate(int fare) {
            return fare + ZERO_FARE;
        }
    }
}
