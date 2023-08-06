package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.SubwayFare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFareTest {
    private static final int 신분당선_추가요금 = 900;
    private Path path;
    private Station 양재역;
    private Station 강남역;
    private Line 신분당선;
    private Member 게스트_사용자;
    private Member 청소년_사용자;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600", 신분당선_추가요금);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        게스트_사용자 = new Member("guest", "password", 0);
        청소년_사용자 = new Member("teen", "password", 15);
    }

    @DisplayName("경로 조회할때 요금계산 시 노선별,거리별 정책이 적용되고 로그인한 사용자의 경우 나이별 정책이 적용된다")
    @Test
    void calculateFare_Login() {
        // given
        Section section = new Section(신분당선, 강남역, 양재역, 16, 5);
        path = createPath(section);

        // when
        int fare = SubwayFare.calculateFare(path, 청소년_사용자);

        // then
        assertThat(fare).isEqualTo(1600);
    }

    @DisplayName("경로 조회할때 요금계산 시 노선별,거리별 정책이 적용되고 비로그인 사용자의 경우 나이별 정책이 적용되지 않는다")
    @Test
    void calculateFare_NotLogin() {
        // given
        Section section = new Section(신분당선, 강남역, 양재역, 16, 5);
        path = createPath(section);

        // when
        int fare = SubwayFare.calculateFare(path, 게스트_사용자);

        // then
        assertThat(fare).isEqualTo(2350);
    }

    private Path createPath(Section... section) {
        Sections sections = new Sections(List.of(section));
        return new Path(sections);
    }
}
