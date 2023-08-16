package nextstep.unit;

import nextstep.domain.subway.Fare.AgeFarePolicy;
import nextstep.domain.subway.Fare.DistanceFarePolicy;
import nextstep.domain.subway.Fare.LineFarePolicy;
import nextstep.domain.subway.Line;
import nextstep.domain.subway.Section;
import nextstep.domain.subway.Station;
import nextstep.util.FareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FarePolicyTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;


    private Long 교대강남구간거리;
    private Long 강남양재구간거리;
    private Long 양재남부터미널구간거리;
    private Long 교대강남구간시간;
    private Long 강남양재구간시간;
    private Long 양재남부터미널구간시간;

    private static int 이호선추가요금;
    private static int 삼호선추가요금;
    private static int 신분당선추가요금;

    private static Line 이호선;
    private static Line 삼호선;
    private static Line 신분당선;

    private Section 교대강남구간;
    private Section 강남양재구간;
    private Section 양재남부터미널구간;



    /**
     * 교대역    --- *2호선* ---   강남역  --- *신분당선 * ---   양재역
     */
    @BeforeEach
    public void setGivenData() {
        //given
        교대역 = Station.builder()
                .id(1L)
                .name("교대역")
                .build();
        강남역 = Station.builder()
                .id(2L)
                .name("강남역")
                .build();
        양재역 = Station.builder()
                .id(3L)
                .name("양재역")
                .build();
        남부터미널역 = Station.builder()
                .id(4L)
                .name("남부터미널역")
                .build();

        이호선추가요금 = 200;
        삼호선추가요금 = 300;
        신분당선추가요금 = 900;

        이호선 = new Line("이호선","Green",이호선추가요금);
        삼호선 = new Line("삼호선","Orange",삼호선추가요금);
        신분당선 = new Line("신분당선","Red",신분당선추가요금);

        교대강남구간거리 = 10L;
        강남양재구간거리 = 15L;
        양재남부터미널구간거리 = 5L;

        교대강남구간시간 = 5L;
        강남양재구간시간 = 5L;
        양재남부터미널구간시간 = 15L;

        교대강남구간 = new Section(이호선, 교대역, 강남역, 교대강남구간거리,교대강남구간시간);
        강남양재구간 = new Section(이호선, 강남역, 양재역, 강남양재구간거리,강남양재구간시간);
        양재남부터미널구간 = new Section(이호선, 양재역, 남부터미널역, 양재남부터미널구간거리,양재남부터미널구간시간);

        이호선.addSection(교대강남구간);
        신분당선.addSection(강남양재구간);
        삼호선.addSection(양재남부터미널구간);

    }

    @DisplayName("구간의 거리 기준 추가요금을 조회")
    @ParameterizedTest
    @MethodSource("provideDistanceAndFare")
    void calculateAdditionalFareByDistance(Long distance,int expectedFare){
        //when
        DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy(distance);

        //then
        assertThat(distanceFarePolicy.calculateFare(0))
                .isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(10L,0),
                Arguments.of(16L,200),
                Arguments.of(60L,800+200)
        );
    }

    @DisplayName("구간의 노선 기준 추가요금을 조회")
    @Test
    void calculateAdditionalFareByLine(){

        //when
        LineFarePolicy lineFarePolicy = new LineFarePolicy(List.of(이호선,신분당선 ));

        assertThat(lineFarePolicy.calculateFare(0))
                .isEqualTo(신분당선추가요금);
    }

    @DisplayName("연령 기준 할인요금을 조회")
    @ParameterizedTest
    @MethodSource("provideAgeAndFare")
    void calculateDiscountedFareByAge(int age ,int originalFare,int expectedFare){

        //when
        AgeFarePolicy ageFarePolicy = new AgeFarePolicy(age);

        assertThat(ageFarePolicy.calculateFare(originalFare))
                .isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideAgeAndFare() {
        return Stream.of(
                Arguments.of(2,1350,0),
                Arguments.of(10,1350,500),
                Arguments.of(15,1350,800),
                Arguments.of(30,1350,1350)
        );
    }

    @DisplayName("상황별 계산된 요금을 조회(구간+노선+나이)")
    @Test
    void calculateFareOfEveryFarePolicy(){

        //when
        int adultTotalFare = FareCalculator.totalFare(교대강남구간거리 + 강남양재구간거리 + 양재남부터미널구간거리, List.of(이호선, 삼호선, 신분당선), 30);
        int childTotalFare = FareCalculator.totalFare(교대강남구간거리 + 강남양재구간거리 + 양재남부터미널구간거리, List.of(이호선, 삼호선, 신분당선), 10);

        //then
        assertThat(adultTotalFare)
                .isEqualTo(1250+400+900);
        assertThat(childTotalFare)
                .isEqualTo((int)((1250+400+900-350)*0.5));

    }
}
