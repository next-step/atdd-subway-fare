package nextstep.subway.unit.line.domain;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import nextstep.subway.line.domain.LineSection2;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("구간 단위 테스트")
class LineSectionTest2 {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();
  private final Station 판교역 = 판교역();

  @DisplayName("구간 앞에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canPrepend() {
    LineSection2 section = new LineSection2(역삼역, 선릉역, 10, 1);
    assertThat(section.canPrepend(new LineSection2(강남역, 역삼역, 20, 2))).isTrue();
    assertThat(section.canPrepend(new LineSection2(강남역, 판교역, 30, 3))).isFalse();
  }

  @DisplayName("구간 뒤에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canAppend() {
    LineSection2 section = new LineSection2(강남역, 역삼역, 10, 1);
    assertThat(section.canAppend(new LineSection2(역삼역, 선릉역, 20, 2))).isTrue();
    assertThat(section.canAppend(new LineSection2(강남역, 판교역, 30, 3))).isFalse();
  }

  @DisplayName("노선 상행역을 공통으로 가운데 새로운 구간을 추가할 수 있는지 확인한다.")
  @Test
  void canSplitUp() {
    LineSection2 section = new LineSection2(강남역, 선릉역, 30, 5);
    assertThat(section.canSplitUp(new LineSection2(강남역, 역삼역, 10, 1))).isTrue();
    assertThat(section.canSplitUp(new LineSection2(역삼역, 선릉역, 10, 1))).isFalse();
    assertThat(section.canSplitUp(new LineSection2(강남역, 역삼역, 40, 4))).isFalse();
    assertThat(section.canSplitUp(new LineSection2(강남역, 역삼역, 20, 5))).isFalse();
    assertThat(section.canSplitUp(new LineSection2(강남역, 역삼역, 20, 6))).isFalse();
  }

  @DisplayName("노선 하행역을 공통으로 가운데 새로운 구간을 추가할 수 있는지 확인한다.")
  @Test
  void canSplitDown() {
    LineSection2 section = new LineSection2(강남역, 선릉역, 30, 3);
    assertThat(section.canSplitDown(new LineSection2(역삼역, 선릉역, 20, 2))).isTrue();
    assertThat(section.canSplitDown(new LineSection2(강남역, 역삼역, 10, 1))).isFalse();
    assertThat(section.canSplitDown(new LineSection2(역삼역, 선릉역, 40, 1))).isFalse();
    assertThat(section.canSplitDown(new LineSection2(강남역, 역삼역, 10, 1))).isFalse();
    assertThat(section.canSplitDown(new LineSection2(역삼역, 선릉역, 20, 4))).isFalse();
  }

  @DisplayName("구간이 같은지 확인한다.")
  @Test
  void isSame() {
    LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);
    assertThat(section.isSame(LineSection2.of(강남역, 역삼역, 10, 1))).isTrue();
    assertThat(section.isSame(LineSection2.of(강남역, 선릉역, 10, 1))).isFalse();
    assertThat(section.isSame(LineSection2.of(선릉역, 역삼역, 10, 1))).isFalse();
    assertThat(section.isSame(LineSection2.of(강남역, 역삼역, 20, 1))).isFalse();
    assertThat(section.isSame(LineSection2.of(강남역, 역삼역, 10, 2))).isFalse();
  }

  @DisplayName("상행역이 같은 경우 구간을 추가하면 구간이 쪼개진다.")
  @Test
  void split_isSameUpStation() {
    LineSection2 section = LineSection2.of(강남역, 선릉역, 30, 3);

    List<LineSection2> sections = section.split(LineSection2.of(강남역, 역삼역, 10, 1));

    assertThat(sections).hasSize(2);
    assertThat(sections.get(0).isSame(LineSection2.of(강남역, 역삼역, 10, 1))).isTrue();
    assertThat(sections.get(1).isSame(LineSection2.of(역삼역, 선릉역, 20, 2))).isTrue();
  }

  @DisplayName("하행역이 같은 경우 구간을 추가하면 구간이 쪼개진다.")
  @Test
  void split_isSameDownStation() {
    LineSection2 section = LineSection2.of(강남역, 선릉역, 30, 3);

    List<LineSection2> sections = section.split(LineSection2.of(역삼역, 선릉역, 10, 1));

    assertThat(sections).hasSize(2);
    assertThat(sections.get(0).isSame(LineSection2.of(강남역, 역삼역, 20, 2))).isTrue();
    assertThat(sections.get(1).isSame(LineSection2.of(역삼역, 선릉역, 10, 1))).isTrue();
  }

  @DisplayName("역이 구간의 상행역이나 하행역인지 확인한다.")
  @Test
  void contains() {
    LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);
    assertThat(section.contains(강남역)).isTrue();
    assertThat(section.contains(역삼역)).isTrue();
    assertThat(section.contains(선릉역)).isFalse();
  }

  @DisplayName("두 구간을 병합한다.")
  @Test
  void merge() {
    LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);

    LineSection2 combinedSection = section.merge(LineSection2.of(역삼역, 선릉역, 20, 2));

    assertThat(combinedSection.isSame(LineSection2.of(강남역, 선릉역, 30, 3))).isTrue();
  }

  @DisplayName("구간을 병합할 수 없는 경우 예외를 던진다.")
  @Test
  void merge_throwsException() {
    LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);
    LineSection2 disjointSection = LineSection2.of(선릉역, 판교역, 20, 2);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> section.merge(disjointSection));
  }
}
