package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void connectBetweenUpStation() {
        Section existSection = new Section(이호선, 강남역, 역삼역, 10, 10);
        Section newSection = new Section(이호선, 선릉역, 역삼역, 10, 10);

        Section section = existSection.replaceDownStationWithUpStation(newSection);

        Assertions.assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(강남역),
                () -> assertThat(section.getDownStation()).isEqualTo(선릉역)
        );
    }

    @DisplayName("하행역 끼리 연결한다.")
    @Test
    void replaceStationWithDownStation() {
        Section existSection = new Section(이호선, 강남역, 역삼역, 10, 10);
        Section newSection = new Section(이호선, 강남역, 선릉역, 3, 3);

        Section section = existSection.replaceDownStationWithDownStation(newSection);

        Assertions.assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(선릉역),
                () -> assertThat(section.getDownStation()).isEqualTo(역삼역),
                () -> assertThat(section.getDistance()).isEqualTo(7),
                () -> assertThat(section.getDuration()).isEqualTo(7)
        );
    }
}
