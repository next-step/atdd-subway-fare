package nextstep.subway.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

	@ParameterizedTest
	@MethodSource("provideGetMaxExtraFareOnLine")
	void getMaxExtraFareOnLine(Sections sections, int expectedMaxExtraFare) {
		// when
		int maxExtraFare = sections.getMaxExtraFareOnLine();

		// then
		assertThat(maxExtraFare).isEqualTo(expectedMaxExtraFare);
	}

	@ParameterizedTest
	@MethodSource("provideGetMaxExtraFareOnLineWhenHasNoExtraFare")
	void getMaxExtraFareOnLineWhenHasNoExtraFare(Sections sections, int expectedMaxExtraFare) {
		// when
		int maxExtraFare = sections.getMaxExtraFareOnLine();

		// then
		assertThat(maxExtraFare).isEqualTo(expectedMaxExtraFare);
	}

	static Stream<Arguments> provideGetMaxExtraFareOnLine() {
		return Stream.of(
				Arguments.of(
						new Sections(Lists.newArrayList(
								new Section(
										new Line("1호선", "red", 100),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10),
								new Section(
										new Line("1호선", "red", 50),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10),
								new Section(
										new Line("1호선", "red", 10),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10))),
						100
				));
	}

	static Stream<Arguments> provideGetMaxExtraFareOnLineWhenHasNoExtraFare() {
		return Stream.of(
				Arguments.of(
						new Sections(Lists.newArrayList(
								new Section(
										new Line("1호선", "red"),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10),
								new Section(
										new Line("1호선", "red"),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10),
								new Section(
										new Line("1호선", "red"),
										new Station("소요산"),
										new Station("동두천"),
										10,
										10))),
						0
				));
	}
}