package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareService {
    private final FareChain basicFareChain;

    public FareService() {
        this.basicFareChain = new FirstFare();
        FareChain secondFareChain = new SecondFare();
        FareChain thirdFareChain = new ThirdFare();

        basicFareChain.setNextChainByChain(secondFareChain);
        secondFareChain.setNextChainByChain(thirdFareChain);
    }

    public int calculateFare(int distance) {
        return basicFareChain.calculateFare(distance);
    }

    public int calculateExtraFare(int fare, List<Line> lines) {
        return fare + lines.stream()
                .mapToInt(Line::getFare)
                .max()
                .orElseThrow(() -> new RuntimeException("노선이 존재하지 않습니다."));
    }
}
