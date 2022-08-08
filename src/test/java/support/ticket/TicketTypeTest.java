package support.ticket;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class TicketTypeTest {

    @ParameterizedTest(name = "[{argumentsWithNames}] 나이에 맞는 티켓이 반환된다.")
    @CsvSource(value = {"5:STANDARD", "6:CHILD", "13:TEENAGER", "19:STANDARD"}, delimiter = ':')
    void ticketTypeOfTest(int age, TicketType expected) {
        assertThat(TicketType.of(age)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{argumentsWithNames}] 각 티켓에 맞게 할인율이 적용된다.")
    @CsvSource(value = {"CHILD:325", "TEENAGER:520", "STANDARD:1000"}, delimiter = ':')
    void extractTotalFareTest(TicketType type, int deductionFare) {
        int fare = 1000;

        assertThat(type.extractTotalFare(fare)).isEqualTo(deductionFare);
    }
}