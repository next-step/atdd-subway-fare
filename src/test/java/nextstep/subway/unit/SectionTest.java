package nextstep.subway.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.Section;

public class SectionTest {

	@DisplayName("소요시간(duration)이 0이하일 때 에러 발생")
	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void createSectionTest(int duration) {
		assertThrows(IllegalArgumentException.class, () ->
			new Section(null, null, null, 10, duration)
		);
	}
}
