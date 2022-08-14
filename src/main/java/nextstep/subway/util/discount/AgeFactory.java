package nextstep.subway.util.discount;

import java.util.ArrayList;
import java.util.List;

public class AgeFactory {

    private final List<DiscountAgePolicy> agePolicies = new ArrayList<>();

    public void addDiscountAgePolicy(DiscountAgePolicy discountAgePolicy) {
        agePolicies.add(discountAgePolicy);
    }

    public DiscountAgePolicy findUsersAge(Integer age) {
        return agePolicies.stream()
                .filter(discountAgePolicy -> discountAgePolicy.support(age))
                .findFirst()
                .orElse(new AdultPaymentPolicy());
    }
}
