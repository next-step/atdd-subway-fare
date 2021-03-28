package nextstep.subway.path.domain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

class ChildDiscountSpecification implements AgeDiscountSpecification {
    private int BASE_DISCOUNT = 350;
    private float DISCOUNT_RATIO = 0.2f;

    @Override
    public int discount(int fare) {
        return (int) ((fare - BASE_DISCOUNT) * (1.0f - DISCOUNT_RATIO));
    }

    @Override
    public boolean apply(int age) {
        if ( age >= 13 && age < 19) {
            return true;
        }
        return false;
    }
}

class YouthDiscountSpecification implements AgeDiscountSpecification {
    private int BASE_DISCOUNT = 350;
    private float DISCOUNT_RATIO = 0.5f;

    @Override
    public int discount(int fare) {
        return (int) ((fare - BASE_DISCOUNT) * (1.0f - DISCOUNT_RATIO));
    }

    @Override
    public boolean apply(int age) {
        if ( age >= 6 && age < 13) {
            return true;
        }
        return false;
    }
}

public class AgeDiscount{

    private List<AgeDiscountSpecification> discountSpecifications = Lists.newArrayList(
        new ChildDiscountSpecification(),
        new YouthDiscountSpecification()
    );

    private Optional< AgeDiscountSpecification > discountSpecification;

    public AgeDiscount(int age){
        this.discountSpecification = discountSpecifications.stream()
                .filter(spec -> spec.apply(age))
                .findFirst();
    }

    public int calculate(int fare){
      if ( this.discountSpecification.isPresent()){
          AgeDiscountSpecification discountSpecification = this.discountSpecification.get();
          return discountSpecification.discount(fare);
      }
      return fare;
    }
}


