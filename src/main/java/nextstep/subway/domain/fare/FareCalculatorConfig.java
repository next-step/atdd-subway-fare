package nextstep.subway.domain.fare;

import nextstep.member.application.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FareCalculatorConfig {

    private final MemberService memberService;

    public FareCalculatorConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    public FareCalculatorChain fareCalculator() {
        FareCalculatorChain defaultFare = new DefaultFareCalculator();
        FareCalculatorChain firstRangeFare = new FirstRangeFareCalculator();
        FareCalculatorChain secondRangeFare = new SecondRangeFareCalculator();
        FareCalculatorChain lineSurchargeFare = new LineSurchargeFareCalculator();
        FareCalculatorChain teenagerDiscountFare = new TeenagerDiscountFareCalculator(memberService);

        defaultFare.setNextChain(firstRangeFare);
        firstRangeFare.setNextChain(secondRangeFare);
        secondRangeFare.setNextChain(lineSurchargeFare);
        lineSurchargeFare.setNextChain(teenagerDiscountFare);

        return defaultFare;
    }
}
