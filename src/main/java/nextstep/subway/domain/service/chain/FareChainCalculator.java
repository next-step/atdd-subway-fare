package nextstep.subway.domain.service.chain;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FareChainCalculator {

    @Qualifier("basicFareCalculator")
    private FareChain baseFareChain;

    @Qualifier("tenToFiftyKiloFareCalculator")
    private FareChain tenToFiftyKiloFareChain;

    @Qualifier("overFiftyKiloFareCalculator")
    private FareChain overFiftyKiloFareChain;

    public int operate(int distance) {
        setChain();
        return baseFareChain.calculateBaseOnDistance(distance);
    }

    private void setChain() {
        baseFareChain.setNext(tenToFiftyKiloFareChain);
        tenToFiftyKiloFareChain.setNext(overFiftyKiloFareChain);
    }
}
