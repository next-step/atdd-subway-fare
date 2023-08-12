package nextstep.subway.domain.service.fee;

import nextstep.member.domain.Member;
import nextstep.subway.domain.service.fee.StationPathDiscountFeeContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class UserAgeDiscountFeeCalculator {
    private static final BigDecimal DISCOUNT_RATE_OF_TEENAGER_USER = BigDecimal.valueOf(0.2);
    private static final BigDecimal DISCOUNT_RATE_OF_CHILDREN_USER = BigDecimal.valueOf(0.5);
    private static final BigDecimal DEDUCTION_PRICE = BigDecimal.valueOf(250);

    public BigDecimal calculateDiscountFee(BigDecimal totalFee, StationPathDiscountFeeContext context) {
        final Member member = context.getMember();

        if(Objects.isNull(member)){
            return BigDecimal.ZERO;
        }

        if (member.isTeenager()) {
            return totalFee.subtract(DEDUCTION_PRICE).multiply(DISCOUNT_RATE_OF_TEENAGER_USER);
        } else if (member.isChildren()) {
            return totalFee.subtract(DEDUCTION_PRICE).multiply(DISCOUNT_RATE_OF_CHILDREN_USER);
        }

        return BigDecimal.ZERO;
    }
}
