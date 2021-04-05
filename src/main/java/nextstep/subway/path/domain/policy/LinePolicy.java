package nextstep.subway.path.domain.policy;

public class LinePolicy extends FarePolicy {

    private int extraFare;

    public LinePolicy(int extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    protected int calculate() {
        System.out.println("extraFare = " + extraFare);
        return extraFare;
    }

    @Override
    protected boolean isValidate() {
        return extraFare >= 0;
    }

}
