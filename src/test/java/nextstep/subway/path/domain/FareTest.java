package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FareTest {

    @Mock
    PathResult pathResult;

    @Mock
    LoginMember member;

    private Sections sections;

    @BeforeEach
    void setUp() {
        Line line = new Line("이호선","green");
        Station upStation = new Station("신촌역");
        Station downStation = new Station("이대역");
        Section section = new Section(line, upStation, downStation, 5, 5);
        sections = new Sections(Arrays.asList(section));
    }

    @ParameterizedTest
    @MethodSource("fareProvider")
    @DisplayName("거리별 금액 계산")
    void calculateOverFare(int distance, int fare) {
        when(pathResult.getSections()).thenReturn(sections);
        when(pathResult.getTotalDistance()).thenReturn(distance);
        FareCalculationStrategy strategy = FareCalculationStrategyFactory.of(pathResult, null);
        assertThat(new Fare(strategy).get()).isEqualTo(fare);
    }

    private static Stream<Arguments> fareProvider() {
        return Stream.of(
                Arguments.of(8, 1250),
                Arguments.of(13, 1350),
                Arguments.of(20, 1450),
                Arguments.of(50, 2050),
                Arguments.of(52, 2250)
        );
    }






}
