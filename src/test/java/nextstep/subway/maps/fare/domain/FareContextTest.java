package nextstep.subway.maps.fare.domain;

import org.junit.jupiter.api.BeforeEach;

class FareContextTest {

    private FareContext fareContext;

    @BeforeEach
    void setUp() {
        fareContext = new FareContext(0);
    }
}