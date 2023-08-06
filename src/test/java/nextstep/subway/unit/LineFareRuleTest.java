package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.LineFareRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineFareRuleTest {
    private static final int 신분당선_추가요금 = 900;
    private static final int 삼호선_추가요금 = 300;
    private static final int 기본요금 = 1250;
    private Station 양재역;
    private Station 강남역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 삼호선;
    private Member 게스트_사용자;
    private Path path;
    private LineFareRule lineFareRule;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        삼호선 = new Line("삼호선", "bg-blue-600", 삼호선_추가요금);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        게스트_사용자 = new Member("guest", "password", 0);

        lineFareRule = new LineFareRule();
    }

    @DisplayName("노선별 정책 - 요금계산 시 추가요금이 있는 노선인 경우 추가요금을 반환한다")
    @Test
    void calculateLineFare() {
        // given
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 8, 5);
        path = createPath(강남역_양재역_구간);

        // when
        int lineSurcharge = lineFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금);

        // then
        assertThat(lineSurcharge).isEqualTo(기본요금 + 신분당선_추가요금);
    }

    @DisplayName("노선별 정책 - 요금계산 시 추가요금이 있는 노선이 여러개인 경우 가장 높은 금액의 추가요금을 반환한다")
    @Test
    void calculateLineFare_MaxSurcharge() {
        // given
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 3, 5);
        Section 남부터미널_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 5, 3);
        path = createPath(강남역_양재역_구간, 남부터미널_양재역_구간);

        // when
        int lineSurcharge = lineFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금);

        // then
        assertThat(lineSurcharge).isEqualTo(기본요금 + 신분당선_추가요금);
    }

    private Path createPath(Section... section) {
        Sections sections = new Sections(List.of(section));
        return new Path(sections);
    }
}
