package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTypeTest {
    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;
    private Section 강남_역삼;

    @BeforeEach
    void Setup() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        이호선 = new Line("2호선", "green");
        강남_역삼 = new Section(이호선, 강남역, 역삼역, 10, 5);


    }

    @DisplayName("거리 유형 입력시 거리값 가져오는지 확인")
    @Test
    void getDistanceByDISTANCE() {
        int distance = PathType.valueOf("DISTANCE").getValue(강남_역삼);

        assertThat(distance).isEqualTo(10);
    }

    @DisplayName("시간 유형 입력시 거리값 가져오는지 확인")
    @Test
    void getDurationByDURATION() {
        int duration = PathType.valueOf("DURATION").getValue(강남_역삼);

        assertThat(duration).isEqualTo(5);
    }

    @DisplayName("유형별로 알맞는 값이 나오는지 확인")
    @ParameterizedTest
    @CsvSource({"DISTANCE, 10", "DURATION, 5"})
    void getValuePerType(String type, int value) {
        assertThat(PathType.valueOf(type).getValue(강남_역삼)).isEqualTo(value);
    }

}
