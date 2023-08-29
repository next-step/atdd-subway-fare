package nextstep.subway.path.domain.chainofresponsibility;

import nextstep.subway.path.domain.Path;

public abstract class BasicFarePolicy implements FarePolicy {
    @Override
    public int calculateFare(Path path) {
        /*
         * 1. 거리에 따른 요금 정책을 적용한다. short, medium, long 중에 하나가 적용된다.
         * 2. 노선의 추가 요금 여부를 체크한다. 가장 큰 추가 요금 하나만 적용된다.
         * 3. 할인 정책을 적용한다. 어린이, 청소년 정책이 존재하고, 정책이 적용되지 않거나 둘 중 하나의 정책이 적용된다.
         */

        return 0;
    }
}
