package nextstep.subway.fare.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.fare.domain.AgeGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeGroupTest {

    @ParameterizedTest
    @CsvSource({
        "6, CHILD",
        "12, CHILD",
        "13, TEENAGER",
        "18, TEENAGER",
        "19, DEFAULT",
        "20, DEFAULT",
        "100, DEFAULT"
    })
    void testAgeGroupSelection(int age, AgeGroup expectedGroup) {
        assertEquals(expectedGroup, AgeGroup.of(age));
    }

    @Test
    void testTeenagerDiscount() {
        AgeGroup teenager = AgeGroup.TEENAGER;
        assertEquals(720, teenager.applyDiscount(1250));
        assertEquals(0, teenager.applyDiscount(350));
    }

    @Test
    void testChildDiscount() {
        AgeGroup child = AgeGroup.CHILD;
        assertEquals(450, child.applyDiscount(1250));
        assertEquals(0, child.applyDiscount(350));
    }

    @Test
    void testDefaultDiscount() {
        AgeGroup defaultGroup = AgeGroup.DEFAULT;
        assertEquals(1250, defaultGroup.applyDiscount(1250));
    }

    @Test
    void testEdgeCases() {
        assertEquals(AgeGroup.DEFAULT, AgeGroup.of(0));
        assertEquals(AgeGroup.DEFAULT, AgeGroup.of(99));
        assertEquals(AgeGroup.DEFAULT, AgeGroup.of(100));
    }
}
