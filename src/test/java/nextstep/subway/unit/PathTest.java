package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {
    public static final Station 강남역 = new Station("강남역");
    public static final Station 양재역 = new Station("양재역");
    public static final Station 교대역 = new Station("교대역");
    public static final Station 남부터미널역 = new Station("남부터미널역");

    static Line 이호선 = new Line("이호선", "green");
    static Line 삼호선 = new Line("이호선", "orange");
    static Line 신분당선 = new Line("신분당선", "red");

    static Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 10, 2);
    static Section 교대_강남_구간 = new Section(이호선, 교대역, 강남역, 10, 2);
    static Section 교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 52, 10);

    @ParameterizedTest
    @DisplayName("요금 계산 기능")
    @MethodSource("provideSectionsAndPrice")
    void calculatePrice(List<Section> sections, int price) {
        Path path = new Path(new Sections(sections));

        assertThat(path.calculatePrice()).isEqualTo(price);
    }

    public static Stream<Arguments> provideSectionsAndPrice() {
        return Stream.of(
                Arguments.of(List.of(강남_양재_구간), 1250),
                Arguments.of(List.of(강남_양재_구간, 교대_강남_구간), 1450),
                Arguments.of(List.of(교대_남부터미널_구간), 2150)
        );
    }
}
