package nextstep.subway.util.fare;


import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class FareCalculator {
    private static final FareChain filterChain;

    private FareCalculator() {
    }

    static {
        FareChain ageFare = new AgeFare();
        FareChain overTenKiloMeterFare = new OverTenKiloMeterFare();
        FareChain overFiftyKiloMeterFare = new OverFiftyKiloMeterFare();
        FareChain surchargeLineFare = new SurchargeLineFare();

        filterChain = ageFare;
        ageFare.connect(overTenKiloMeterFare);
        overTenKiloMeterFare.connect(overFiftyKiloMeterFare);
        overFiftyKiloMeterFare.connect(surchargeLineFare);
    }

    public static int calculate(Path path, Member member) {
        return filterChain.calculate(path, member);
    }
}
