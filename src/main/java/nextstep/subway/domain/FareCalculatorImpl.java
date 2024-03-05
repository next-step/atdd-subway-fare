package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.fareOption.Fare10KmTo50KmDistanceOption;
import nextstep.subway.domain.fareOption.Fare50KmOverDistanceOption;
import nextstep.subway.domain.fareOption.FareCalculateOption;
import nextstep.subway.domain.fareOption.FareDiscountChildrenOption;
import nextstep.subway.domain.fareOption.FareDiscountOption;
import nextstep.subway.domain.fareOption.FareDiscountTeensOption;
import nextstep.subway.domain.fareOption.FareExtraFareDistanceOption;
import org.springframework.stereotype.Component;

@Component
public class FareCalculatorImpl implements FareCalculator {
    private static final int DEFAULT_FARE = 1_250;

    private final List<FareCalculateOption> fareCalculateOptions;
    private final List<FareDiscountOption> fareDiscountOptions;

    public FareCalculatorImpl() {
        this(List.of(
            new Fare10KmTo50KmDistanceOption(),
            new Fare50KmOverDistanceOption(),
            new FareExtraFareDistanceOption()
        ), List.of(
            new FareDiscountChildrenOption(),
            new FareDiscountTeensOption()
        ));
    }

    public FareCalculatorImpl(List<FareCalculateOption> fareCalculateOptions,
        List<FareDiscountOption> fareDiscountOptions) {
        this.fareCalculateOptions = fareCalculateOptions;
        this.fareDiscountOptions = fareDiscountOptions;
    }

    public int calculateFare(int distance, Set<LineResponse> line, Optional<Member> member) {
        final Integer fare = fareCalculateOptions.stream()
            .filter(calculateOption -> calculateOption.isCalculateTarget(distance, line))
            .mapToInt(calculateOption -> calculateOption.calculateFare(distance, line))
            .reduce(DEFAULT_FARE, Integer::sum);

        return member.map(value -> {
            final var ref = new Object() {
                int calculatedFare = fare.intValue();
            };

            fareDiscountOptions.stream()
                .filter(discountOption -> discountOption.isDiscountTarget(value))
                .forEach(discountOption -> ref.calculatedFare = discountOption.calculateFare(ref.calculatedFare));

            return ref.calculatedFare;
        }).orElse(fare);
    }
}
