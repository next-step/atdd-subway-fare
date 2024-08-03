package nextstep.subway.unit.line.domain;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import nextstep.subway.line.domain.LineSection2;
import nextstep.subway.line.domain.LineSections2;
import nextstep.subway.line.exception.CannotAddLineSectionException;
import nextstep.subway.line.exception.CannotRemoveLastLineSectionException;
import nextstep.subway.line.exception.LineSectionAlreadyExistsException;
import nextstep.subway.line.exception.StationNotFoundInLineException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("구간 단위 테스트")
class LineSectionsTest2 {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();

  @DisplayName("구간 추가 단위 테스트")
  @Nested
  class AddLineSectionTest {
    @DisplayName("노선에 구간이 없는 경우 새 구간을 등록한다.")
    @Test
    void shouldAddWhenEmpty() {
      LineSections2 sections = new LineSections2();
      LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(1);
      assertThat(sections.getFirst().isSame(section)).isTrue();
      assertThat(sections.getFirst().getDistance()).isEqualTo(section.getDistance());
      assertThat(sections.getFirst().getDuration()).isEqualTo(section.getDuration());
    }

    @DisplayName("기존 구간 뒤에 새로운 구간을 추가한다.")
    @Test
    void addShouldAppend() {
      LineSections2 sections = new LineSections2(강남역, 역삼역, 10, 1);
      LineSection2 section = LineSection2.of(역삼역, 선릉역, 20, 2);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      assertThat(sections.getLast().isSame(section)).isTrue();
      assertThat(sections.getLast().getDistance()).isEqualTo(section.getDistance());
      assertThat(sections.getLast().getDuration()).isEqualTo(section.getDuration());
    }

    @DisplayName("기존 구간 뒤에 새로운 구간을 추가할 때 이미 등록되어 있는 역은 등록될 수 없다.")
    @Test
    void appendShouldThrowAlreadyExistsException() {
      LineSections2 sections =
          new LineSections2(
              Arrays.asList(LineSection2.of(강남역, 역삼역, 10, 1), LineSection2.of(역삼역, 선릉역, 20, 2)));
      LineSection2 section = LineSection2.of(선릉역, 역삼역, 30, 3);
      assertThatExceptionOfType(LineSectionAlreadyExistsException.class)
          .isThrownBy(() -> sections.add(section));
    }

    @DisplayName("기존 구간 앞에 새로운 구간을 추가한다.")
    @Test
    void addShouldPrepend() {
      LineSections2 sections = new LineSections2(역삼역, 선릉역, 20, 2);
      LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      assertThat(sections.getFirst().isSame(section)).isTrue();
      assertThat(sections.getFirst().getDistance()).isEqualTo(section.getDistance());
      assertThat(sections.getFirst().getDuration()).isEqualTo(section.getDuration());
    }

    @DisplayName("기존 구간 앞에 새로운 구간을 추가할 때 이미 등록된 있는 역은 등록될 수 없다.")
    @Test
    void prependShouldThrowAlreadyExistsException() {
      LineSections2 sections =
          new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1), LineSection2.of(역삼역, 선릉역, 20, 2));
      LineSection2 section = LineSection2.of(역삼역, 강남역, 30, 3);
      assertThatExceptionOfType(LineSectionAlreadyExistsException.class)
          .isThrownBy(() -> sections.add(section));
    }

    @DisplayName("상행역이 같은 구간을 추가하는 경우 가운데 하행역이 추가된다.")
    @Test
    void addShouldInsertWhenUpStationsAreTheSame() {
      LineSections2 sections = new LineSections2(강남역, 선릉역, 30, 3);
      LineSection2 section = LineSection2.of(강남역, 역삼역, 10, 1);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      assertThat(sections.getFirst().isSame(section)).isTrue();
      assertThat(sections.getFirst().getDistance()).isEqualTo(section.getDistance());
      assertThat(sections.getLast().isSame(LineSection2.of(역삼역, 선릉역, 20, 2))).isTrue();
      assertThat(sections.getLast().getDistance()).isEqualTo(20);
      assertThat(sections.getLast().getDuration()).isEqualTo(2);
    }

    @DisplayName("상행역이 같은 구간을 추가하는 경우 구간 길이가 이전 구간 길이보다 길거나 같으면 예외 처리된다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 20})
    void addShouldNotInsertWhenUpStationsAreTheSameButDistanceTooLong(int distance) {
      LineSections2 sections = new LineSections2(강남역, 선릉역, 10, 2);
      LineSection2 section = LineSection2.of(강남역, 역삼역, distance, 1);
      assertThatExceptionOfType(CannotAddLineSectionException.class)
          .isThrownBy(() -> sections.add(section));
    }

    @DisplayName("상행역이 같은 구간을 추가하는 경우 소요시간이 이전 구간 시간보다 길거나 같으면 예외 처리된다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 20})
    void addShouldThrowExceptionWhenDurationIsTooLong(int duration) {
      LineSections2 sections = new LineSections2(강남역, 선릉역, 20, 10);
      LineSection2 section = LineSection2.of(강남역, 역삼역, 10, duration);
      assertThatExceptionOfType(CannotAddLineSectionException.class)
          .isThrownBy(() -> sections.add(section));
    }

    @DisplayName("하행역이 같은 구간을 추가하는 경우 가운데 상행역이 추가된다.")
    @Test
    void addShouldInsertWhenDownStationsAreTheSame() {
      LineSections2 sections = new LineSections2(강남역, 선릉역, 30, 3);
      LineSection2 section = LineSection2.of(역삼역, 선릉역, 20, 2);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      assertThat(sections.getFirst().isSame(LineSection2.of(강남역, 역삼역, 10, 1))).isTrue();
      assertThat(sections.getFirst().getDistance()).isEqualTo(10);
      assertThat(sections.getFirst().getDuration()).isEqualTo(1);
      assertThat(sections.getLast().isSame(section)).isTrue();
      assertThat(sections.getLast().getDistance()).isEqualTo(section.getDistance());
      assertThat(sections.getLast().getDuration()).isEqualTo(section.getDuration());
    }

    @DisplayName("하행역이 같은 구간을 추가하는 경우 구간 길이가 이전 구간 길이보다 길거나 같으면 예외 처리된다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 20})
    void addShouldNotInsertWhenDownStationsAreTheSameButDistanceTooLong(int distance) {
      LineSections2 sections = new LineSections2(강남역, 선릉역, 10, 5);
      LineSection2 section = LineSection2.of(역삼역, 선릉역, distance, 1);
      assertThatExceptionOfType(CannotAddLineSectionException.class)
          .isThrownBy(() -> sections.add(section));
    }
  }

  @DisplayName("하행역이 같은 구간을 추가하는 경우 소요시간이 이전 구간 소요시간 보다 길거나 같으면 예외 처리된다.")
  @ParameterizedTest
  @ValueSource(ints = {10, 11, 20})
  void addShouldThrowExceptionWhenDurationTooLong(int duration) {
    LineSections2 sections = new LineSections2(강남역, 선릉역, 20, 10);
    LineSection2 section = LineSection2.of(역삼역, 선릉역, 10, duration);
    assertThatExceptionOfType(CannotAddLineSectionException.class)
        .isThrownBy(() -> sections.add(section));
  }

  @DisplayName("구간 제거 단위 테스트")
  @Nested
  class RemoveLineSectionTest {
    @DisplayName("노선에 역이 등록되어 있지 않은 경우 예외를 던진다.")
    @Test
    void shouldThrowExceptionWhenStationNotFound() {
      LineSections2 sections = new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1));
      assertThatExceptionOfType(StationNotFoundInLineException.class)
          .isThrownBy(() -> sections.remove(선릉역));
    }

    @DisplayName("노선에 구간이 하나만 있는 경우 예외를 던진다.")
    @Test
    void shouldThrowExceptionWhenLastSection() {
      LineSections2 sections = new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1));
      assertThatExceptionOfType(CannotRemoveLastLineSectionException.class)
          .isThrownBy(() -> sections.remove(역삼역));
    }

    @DisplayName("하행 종점역을 제거한다.")
    @Test
    void shouldRemoveTerminalDownStation() {
      LineSections2 sections =
          new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1), LineSection2.of(역삼역, 선릉역, 20, 2));

      sections.remove(선릉역);

      assertThat(sections.size()).isEqualTo(1);
      assertThat(sections.getFirst().isSame(LineSection2.of(강남역, 역삼역, 10, 1))).isTrue();
    }

    @DisplayName("상행 종점역을 제거한다.")
    @Test
    void shouldRemoveTerminalUpStation() {
      LineSections2 sections =
          new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1), LineSection2.of(역삼역, 선릉역, 20, 2));

      sections.remove(강남역);

      assertThat(sections.size()).isEqualTo(1);
      assertThat(sections.getLast().isSame(LineSection2.of(역삼역, 선릉역, 20, 2))).isTrue();
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void shouldRemoveMiddleStation() {
      LineSections2 sections =
          new LineSections2(LineSection2.of(강남역, 역삼역, 10, 1), LineSection2.of(역삼역, 선릉역, 20, 2));

      sections.remove(역삼역);

      assertThat(sections.size()).isEqualTo(1);
      assertThat(sections.getFirst().isSame(LineSection2.of(강남역, 선릉역, 30, 3))).isTrue();
    }
  }
}
