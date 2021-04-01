package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("요금 조회 테스트")
class FareTest {

    private PathResult pathResult;

    @Mock
    private Stations stations;

    @Mock
    private Sections sections;

    @BeforeEach
    void setUp() {
        pathResult = new PathResult(stations, sections);
    }

    @ParameterizedTest(name = "[거리: {0}km / 추가요금 노선 {1}개 / 나이: {2}] 일 경우 -> {3}원")
    @CsvFileSource(resources = "/fare-test-input.csv", numLinesToSkip = 1)
    void getTotalFare1(int distance, int addFareLineCount, int age, int fare) {
        // given
        when(sections.getTotalDistance()).thenReturn(distance);
        when(sections.getSections()).thenReturn(makeSections(addFareLineCount));

        // when
        int totalFare = pathResult.getTotalFare(age);

        // then
        assertThat(totalFare).isEqualTo(fare);
    }

    private ArrayList<Section> makeSections(int addFareLineCount) {
        ArrayList<Section> returnSections = new ArrayList<>();
        if (addFareLineCount >= 1) {
            Line line = new Line("2호선", "green", 500);
            returnSections.add(new Section(line, new Station(), new Station(), 10, 10));
        }
        if (addFareLineCount >= 2) {
            Line line = new Line("3호선", "orange", 900);
            returnSections.add(new Section(line, new Station(), new Station(), 10, 10));
        }
        return returnSections;
    }
}
