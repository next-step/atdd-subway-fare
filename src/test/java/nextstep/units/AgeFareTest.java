package nextstep.units;

import nextstep.member.domain.AgeRange;
import nextstep.path.fare.age.AgeFare;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgeFareTest {
    private AgeFare ageFare;

    @Test
    public void testChildrenFare() {
        ageFare = AgeFare.from(AgeRange.CHILDREN);
        int totalFare = ageFare.calculate(1250);

        assertEquals(450, totalFare);
    }

    @Test
    public void testTeenagerFare() {
        ageFare = AgeFare.from(AgeRange.TEENAGER);
        int totalFare = ageFare.calculate(1250);

        assertEquals(720, totalFare);
    }
}
