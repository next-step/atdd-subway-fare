package nextstep.subway.applicaion;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private static final int 기본_요금 = 1250;

    FareCalculator fareCalculator = new FareCalculator();

    @ParameterizedTest(name = "{index}: {3}: {0}km 이용 = {2}원")
    @MethodSource("이용_거리_0km")
    void 이용_거리가_0이면_요금도_0원이다(int distance, int age, int price, String message) {
        거리에_따른_가격_비교(distance, age, price);
    }

    @ParameterizedTest(name = "{index}: {3}: {0}km 이용 = {2}원")
    @MethodSource("이용_거리_10km이하")
    void 이용_거리가_10km이하는_기본_요금이_나와야_한다(int distance, int age, int price, String message) {
        거리에_따른_가격_비교(distance, age, price);
    }

    @ParameterizedTest(name = "{index}: {3}: {0}km 이용 = {2}원")
    @MethodSource("이용_거리_10km초과_50km이하")
    void 이용_거리가_10km초과_50km이하는_5km마다_100원의_추가요금이_나와야_한다(int distance, int age, int price, String message) {
        거리에_따른_가격_비교(distance, age, price);
    }

    @ParameterizedTest(name = "{index}: {3}: {0}km 이용 = {2}원")
    @MethodSource("이용_거리_50km초과")
    void 이용_거리가_50km초과분은_8km마다_100원의_추가요금이_나와야_한다(int distance, int age, int price, String message) {
        거리에_따른_가격_비교(distance, age, price);
    }

    private static int 연령별_요금(int age, int price) {
        if (age < 13 && age >= 6) {
            return (price - 350) / 10 * 5;
        } else if (age < 19 && age >= 13) {
            return (price - 350) / 10 * 8;
        }
        return price;
    }

    private static Stream<Arguments> 이용_거리_0km() {
        return Stream.of(
                Arguments.of(0, 6, 0, "어린이"),
                Arguments.of(0, 16, 0, "청소년"),
                Arguments.of(0, 26, 0, "성인")
        );
    }

    private static Stream<Arguments> 이용_거리_10km이하() {
        return Stream.of(
                Arguments.of(1, 6, 연령별_요금(6, 기본_요금), "어린이"),
                Arguments.of(1, 16, 연령별_요금(16, 기본_요금), "청소년"),
                Arguments.of(1, 26, 연령별_요금(26, 기본_요금), "성인"),

                Arguments.of(10, 6, 연령별_요금(6, 기본_요금), "어린이"),
                Arguments.of(10, 16, 연령별_요금(16, 기본_요금), "청소년"),
                Arguments.of(10, 26, 연령별_요금(26, 기본_요금), "성인")
        );
    }

    /**
     * 11km = 10km + 1km = 기본_요금 + ceil(1/5)*100
     * 37km = 10km + 27km = 기본_요금 + ceil(27/5)*100
     * 50km = 10km + 40km = 기본_요금 + ceil(40/5)*100
     */
    private static Stream<Arguments> 이용_거리_10km초과_50km이하() {
        final int 이용거리_11km_요금 = 기본_요금 + 100;
        final int 이용거리_37km_요금 = 기본_요금 + 600;
        final int 이용거리_50km_요금 = 기본_요금 + 800;

        return Stream.of(
                Arguments.of(11, 6, 연령별_요금(6, 이용거리_11km_요금), "어린이"),
                Arguments.of(11, 16, 연령별_요금(16, 이용거리_11km_요금), "청소년"),
                Arguments.of(11, 26, 연령별_요금(26, 이용거리_11km_요금), "성인"),

                Arguments.of(37, 6, 연령별_요금(6, 이용거리_37km_요금), "어린이"),
                Arguments.of(37, 16, 연령별_요금(16, 이용거리_37km_요금), "청소년"),
                Arguments.of(37, 26, 연령별_요금(26, 이용거리_37km_요금), "성인"),

                Arguments.of(50, 6, 연령별_요금(6, 이용거리_50km_요금), "어린이"),
                Arguments.of(50, 16, 연령별_요금(16, 이용거리_50km_요금), "청소년"),
                Arguments.of(50, 26, 연령별_요금(26, 이용거리_50km_요금), "성인")
        );
    }

    /**
     * 51km = 10km + 40km + 1km = 기본_요금 + 8*100 + ceil(1/8)*100
     * 107km = 10km + 40km + 56km = 기본_요금 + 8*100 + ceil(57/8)*100
     */
    private static Stream<Arguments> 이용_거리_50km초과() {
        final int 이용거리_51km_요금 = 기본_요금 + 800 + 100;
        final int 이용거리_107km_요금 = 기본_요금 + 800 + 800;

        return Stream.of(
                Arguments.of(51, 6, 연령별_요금(6, 이용거리_51km_요금), "어린이"),
                Arguments.of(51, 16, 연령별_요금(16, 이용거리_51km_요금), "청소년"),
                Arguments.of(51, 26, 연령별_요금(26, 이용거리_51km_요금), "성인"),

                Arguments.of(107, 6, 연령별_요금(6, 이용거리_107km_요금), "어린이"),
                Arguments.of(107, 16, 연령별_요금(16, 이용거리_107km_요금), "청소년"),
                Arguments.of(107, 26, 연령별_요금(26, 이용거리_107km_요금), "성인")
        );
    }

    private void 거리에_따른_가격_비교(int distance, int age, int price) {
        int calculatedFare = fareCalculator.calculateOverFare(distance, 0, age);

        assertThat(calculatedFare).isEqualTo(price);
    }
}
