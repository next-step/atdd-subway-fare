package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class FareCalculateServiceTest {

    private final FareCalculateService fareCalculateService = new FareCalculateService();

    @DisplayName("추가 요금이 없는 노선인 경우 거리에 따른 요금만이 발생한다.")
    @ParameterizedTest(name = "거리가 {0}km일 때 요금은 총 {1}원이다.")
    @MethodSource("distanceSource")
    void fares_vary_depending_on_the_distance(int distance, int fare) {
        // given
        Path path = mock(Path.class);
        given(path.extractDistance()).willReturn(distance);
        given(path.getLineAdditionalFare()).willReturn(0);

        // when
        int sut = fareCalculateService.calculateFareAmount(path);

        // then
        assertThat(sut).isEqualTo(fare);
    }

    private static Stream<Arguments> distanceSource() {
        return Stream.of(
            Arguments.of(9, 1250),
            Arguments.of(10, 1250),
            Arguments.of(15, 1350),
            Arguments.of(16, 1450),
            Arguments.of(50, 2050),
            Arguments.of(58, 2150),
            Arguments.of(59, 2250)
        );
    }

    @DisplayName("추가 요금이 있는 노선을 거치는 경우 거리에 따른 요금에 추가 요금이 붙는다.")
    @Test
    void additional_fares_apply_for_certain_lines() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line line = new Line("2호선", "green", 1100);
        line.addSection(교대역, 강남역, 5, 5);

        SubwayMap subwayMap = new SubwayMap(List.of(line));
        Path path = subwayMap.findPath(교대역, 강남역, Section::getDistance);

        // when
        int sut = fareCalculateService.calculateFareAmount(path);

        // then
        assertThat(sut).isEqualTo(2350);
    }

    @DisplayName("환승을 하는 경우 추가 요금이 가장 높은 노선의 추가 요금이 붙는다.")
    @Test
    void when_transferring_the_highest_fare_applies() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Line line1 = new Line("2호선", "green", 1100);
        line1.addSection(교대역, 강남역, 5, 5);
        Line line2 = new Line("신분당선", "red", 1300);
        line2.addSection(강남역, 양재역, 5, 5);

        SubwayMap subwayMap = new SubwayMap(List.of(line1, line2));
        Path path = subwayMap.findPath(교대역, 양재역, Section::getDistance);

        // when
        int sut = fareCalculateService.calculateFareAmount(path);

        // then
        assertThat(sut).isEqualTo(2550);
    }

    @DisplayName("13세 이상부터 19세 미만의 청소년은 운임에서 350원을 공제한 금액의 20%가 할인된다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void discount_apply_for_youth(int age) {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line line = new Line("2호선", "green", 1100);
        line.addSection(교대역, 강남역, 5, 5);

        SubwayMap subwayMap = new SubwayMap(List.of(line));
        Path path = subwayMap.findPath(교대역, 강남역, Section::getDistance);

        // when
        int sut = fareCalculateService.calculateFareAmount(path, age);

        // then
        assertThat(sut).isEqualTo(1950);
    }

    @DisplayName("6세 이상부터 13세 미만의 어린이는 운임에서 350원을 공제한 금액의 50%가 할인된다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void discount_apply_for_children(int age) {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line line = new Line("2호선", "green", 1100);
        line.addSection(교대역, 강남역, 5, 5);

        SubwayMap subwayMap = new SubwayMap(List.of(line));
        Path path = subwayMap.findPath(교대역, 강남역, Section::getDistance);

        // when
        int sut = fareCalculateService.calculateFareAmount(path, age);

        // then
        assertThat(sut).isEqualTo(1350);
    }

    @DisplayName("할인 금액은 소수점 이하에서 내림한다")
    @Test
    void round_down_to_decimal_places() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line line = new Line("2호선", "green", 73);
        line.addSection(교대역, 강남역, 5, 5);

        SubwayMap subwayMap = new SubwayMap(List.of(line));
        Path path = subwayMap.findPath(교대역, 강남역, Section::getDistance);

        // when
        int sut = fareCalculateService.calculateFareAmount(path, 13);

        // then
        assertThat(sut).isEqualTo(1129);
    }
}
