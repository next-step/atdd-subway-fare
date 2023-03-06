package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import nextstep.subway.domain.exception.SectionCreateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("구간 관련 기능")
class SectionTest {

    private Line 이호선;
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
    }

    @DisplayName("상행역 끼리 연결한다.")
    @Test
    void replaceDownStationWithUpStation() {
        Section existSection = new Section(이호선, 강남역, 역삼역, 10, 10);
        Section newSection = new Section(이호선, 선릉역, 역삼역, 3, 3);

        Section section = existSection.replaceDownStationWithUpStation(newSection);

        Assertions.assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(강남역),
                () -> assertThat(section.getDownStation()).isEqualTo(선릉역),
                () -> assertThat(section.getDistance()).isEqualTo(7),
                () -> assertThat(section.getDuration()).isEqualTo(7)
        );
    }

    @DisplayName("하행역 끼리 연결한다.")
    @Test
    void replaceUpStationWithDownStation() {
        Section existSection = new Section(이호선, 강남역, 역삼역, 10, 10);
        Section newSection = new Section(이호선, 강남역, 선릉역, 3, 3);

        Section section = existSection.replaceUpStationWithDownStation(newSection);

        Assertions.assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(선릉역),
                () -> assertThat(section.getDownStation()).isEqualTo(역삼역),
                () -> assertThat(section.getDistance()).isEqualTo(7),
                () -> assertThat(section.getDuration()).isEqualTo(7)
        );
    }

    private static Stream<Arguments> provideForCreateSectionFail() {
        Station upStation = new Station("강남역");
        Station downStation = new Station("역삼역");
        Line line = new Line("2호선", "green");

        return Stream.of(
                Arguments.of(null, upStation, downStation, 10, 10),
                Arguments.of(line, null, downStation, 10, 10),
                Arguments.of(line, upStation, null, 10, 10),
                Arguments.of(line, upStation, downStation, 0, 10),
                Arguments.of(line, upStation, downStation, 10, 0)
        );
    }

    @DisplayName("구간 생성시 조건에 맞지 않으면 추가하지 않는다.")
    @ParameterizedTest(name = "상행역 : {0}, 하행역 : {1}, 거리 : {2}, 시간 : {3}")
    @MethodSource("provideForCreateSectionFail")
    void createSectionFail(Line line, Station upStation, Station downStation, int distance, int duration) {
        assertThatThrownBy(() -> new Section(line, upStation, downStation, distance, duration))
                .isInstanceOf(SectionCreateException.class);
    }
}
