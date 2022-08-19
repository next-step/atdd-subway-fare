package nextstep.subway.domain.service.chain;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.service.FareChainType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FareChainCalculator {

    private final Map<String, FareChain> fareChainMap;

    public int operate(int distance) {
        setChain();
        FareChain basicChain = getBasicChain();
        return basicChain.calculateBaseOnDistance(distance);
    }

    private void setChain() {
        FareChain basicFareChain = getBasicChain();
        FareChain mediumDistancePassengerChain = getMediumDistanceFareChain();
        FareChain longDistancePassengerChain = getLongDistanceFareChain();

        basicFareChain.setNext(mediumDistancePassengerChain);
        mediumDistancePassengerChain.setNext(longDistancePassengerChain);
    }

    private FareChain getLongDistanceFareChain() {
        return fareChainMap.get(FareChainType.LONG_DISTANCE_PASSENGER_CHAIN.implementation());
    }

    private FareChain getMediumDistanceFareChain() {
        return fareChainMap.get(FareChainType.MEDIUM_DISTANCE_PASSENGER_CHAIN.implementation());
    }

    private FareChain getBasicChain() {
        return fareChainMap.get(FareChainType.BASIC_CHAIN.implementation());
    }
}
