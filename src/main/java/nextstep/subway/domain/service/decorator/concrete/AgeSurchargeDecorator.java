package nextstep.subway.domain.service.decorator.concrete;

import lombok.AllArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.domain.service.decorator.SurchargeDecorator;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;

import java.util.Arrays;
import java.util.function.ToIntFunction;

public class AgeSurchargeDecorator extends SurchargeDecorator {

    private final Member member;

    public AgeSurchargeDecorator(SurchargeCalculator surchargeCalculator, Member member) {
        super(surchargeCalculator);
        this.member = member;
    }

    @Override
    public int appendFare() {
        return AgeSurchargeCalculate.calculator(member.getAge(), super.appendFare());
    }
}

@AllArgsConstructor
enum AgeSurchargeCalculate {
    CHILDREN(6, 13, baseFare -> (int) ((baseFare - 350) * 0.5)),
    TEENAGER(13, 19, baseFare -> (int) ((baseFare - 350) * 0.8)),
    ADULT(19, Integer.MAX_VALUE, baseFare -> baseFare);

    private final int min;
    private final int max;
    private final ToIntFunction<Integer> operate;

    public static int calculator(int age, int fare) {
        return getInRange(age).operate.applyAsInt(fare);
    }

    private static AgeSurchargeCalculate getInRange(int age) {
        return Arrays.stream(values())
                     .filter(v -> age >= v.min &&  age < v.max)
                     .findAny()
                     .orElse(ADULT);
    }

}

