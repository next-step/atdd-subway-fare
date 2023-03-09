package nextstep.subway.domain;

public class LineSurchargePolicy {

    public static int calculate(Path path) {
        return path.getSections().getMaxSurcharge();
    }
}
