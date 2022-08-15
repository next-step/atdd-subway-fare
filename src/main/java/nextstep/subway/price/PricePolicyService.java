package nextstep.subway.price;

import java.util.Objects;
import nextstep.subway.price.discount.DiscountPolicy;
import nextstep.subway.price.distance.DistancePricePolicy;

public class PricePolicyService implements PricePolicy {

    private Integer totalDistance;
    private Integer age;
    private Integer addtionPrice;

    public PricePolicyService(Integer totalDistance, Integer age, Integer addtionPrice) {
        this.totalDistance = totalDistance;
        this.age = age;
        this.addtionPrice = addtionPrice;
    }

    /**
     * 요금 계산 방법
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     *
     * -- 추가 사항
     *
     * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
     * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
     * -> 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
     */
    @Override
    public int calculatePrice() {
        DistancePricePolicy distancePricePolicy = new DistancePricePolicy(totalDistance);
        int price = distancePricePolicy.calculatePrice();  // 거리 계산
        price = this.addPrice(price);                             // 추가 욕금 추가

        return this.discount(price);    // 나이별 할인 혜택
    }

    private int addPrice(Integer price) {
        return price + this.addtionPrice;
    }

    private int discount(Integer price) {
        if(Objects.isNull(this.age)) {
            return price;
        }
        return DiscountPolicy.discount(price, this.age);
    }

}
