package nextstep.subway.unit;

import nextstep.subway.domain.service.chain.FareChain;
import nextstep.subway.domain.service.chain.FareChainCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FareChainTest {

    @Autowired
    @Qualifier("basicFareCalculator")
    private FareChain basicChain;

    @Autowired
    @Qualifier("mediumDistancePassengerFareCalculator")
    private FareChain tenToFiftyChain;

    @Autowired
    @Qualifier("longDistancePassengerFareCalculator")
    private FareChain overToFiftyChain;

    private FareChainCalculator fareChainCalculator;

    @BeforeEach
    void setUp() {
        Map<String, FareChain> fareChainMap = Map.of(
                "basicFareCalculator", basicChain,
                "mediumDistancePassengerFareCalculator", tenToFiftyChain,
                "longDistancePassengerFareCalculator", overToFiftyChain
        );

        fareChainCalculator = new FareChainCalculator(fareChainMap);
    }

    /*
     * 거리가 10일 경우 기본요금 1250
     * 거리가 11일 경우 1350 (5km 마다 100원씩 추가요금 부여)
     * 거리가 51일 경우 2150 (8km 마다 100원씩 추가요금 부여)
     */
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "51:2150"}, delimiter = ':')
    void calculate(String distance, String fare) {
        int extractFare = fareChainCalculator.operate(Integer.parseInt(distance));

        assertThat(extractFare).isEqualTo(Integer.parseInt(fare));
    }


}
