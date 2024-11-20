package nextstep.subway.domain.fare;

import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@Priority(1)
public class DistanceFarePolicy implements FarePolicy {
    @Override
    public boolean support(FarePolicyContext context) {
        return context.getdistance() != null;
    }

    @Override
    public Fare invoke(FarePolicyContext context, Fare fare) {
        Long distance = context.getdistance();
        if (distance == null) {
            throw new IllegalArgumentException("조회된 경로의 총 거리가 유효하지 않습니다.");
        }

        if (distance <= 10) {
            return fare;
        }

        if (distance <= 50) {
            long until50km = distance - 10;
            fare.addFare((long) ((Math.ceil((until50km - 1) / 5) + 1) * 100));
            return fare;
        }

        // 50km까지 요금
        long until50km = 40;
        fare.addFare((long) ((Math.ceil((until50km - 1) / 5) + 1) * 100));

        // 50km 초과 요금
        long over50km = distance - 50;
        fare.addFare((long) ((Math.ceil((over50km - 1) / 8) + 1) * 100));

        return fare;
    }
}
