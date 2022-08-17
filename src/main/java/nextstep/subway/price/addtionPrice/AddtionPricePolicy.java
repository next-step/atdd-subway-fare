package nextstep.subway.price.addtionPrice;

import nextstep.subway.price.PricePolicy;

public class AddtionPricePolicy implements PricePolicy {
    private Integer price;
    private Integer addtionPrice;

    public AddtionPricePolicy(Integer price, Integer addtionPrice) {
        this.price = price;
        this.addtionPrice = addtionPrice;
    }

    @Override
    public int calculatePrice() {
        return price + addtionPrice;
    }
}
