package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

	@Mock
	Path path;

	FareService fareService;

	@BeforeEach
	void setUp() {
		fareService = new FareService();
	}

	@Test
	void calculateAmount() {
		// given
		when(path.extractDistance()).thenReturn(10);
		when(path.getExtraFareOnLine()).thenReturn(50);

		// when
		Fare fare = fareService.calculateAmount(path, 18);

		// then
		assertThat(fare).isEqualTo(Fare.valueOf((int) ((1250 + 50 - 350) * 0.8)));
	}
}