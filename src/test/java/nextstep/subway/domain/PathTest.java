package nextstep.subway.domain;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     *
     * // @formatter:off
     *               10km, 5분, 100원
     *         교대역 ---- 2호선 ---- 강남역
     *           |                  |
     *           |                  |
     *           |                  |
     *         3호선              신분당선
     *      3km, 20분, 1000원     5km, 10분, 1000원
     *           |                  |
     *           |                  |
     *           |                  |
     *        남부터미널역--- 3호선 ----양재역
     *              5km, 10분, 2000원
     * // @formatter:on
     */
    @BeforeEach
    void setUp() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("이호선", "green", 100);
        신분당선 = new Line("신분당선", "green", 1000);
        삼호선 = new Line("삼호선", "green", 200);

        이호선.addSection(교대역, 강남역, 10, 5);
        신분당선.addSection(강남역, 양재역, 5, 10);
        삼호선.addSection(남부터미널역, 양재역, 10, 10);
        삼호선.addSection(교대역, 남부터미널역, 3, 20);

        List<Section> allSections = new ArrayList<>();

        allSections.addAll(이호선.getSections());
        allSections.addAll(삼호선.getSections());
        allSections.addAll(신분당선.getSections());

    }

    /**
     * When 멤버가 아닌 사람이 요금 요청시
     * Then 요금이 계산이 된다
     */
    @DisplayName("멤버가 아닌 회원이 요금 요청시 요금이 계산된다")
    @Test
    void calcFareForNotMember() {
        // Given
        Sections sections = new Sections(Stream.of(이호선.getSections(), 신분당선.getSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Path path = new Path(sections);

        // When
        path.calcFareForNotMember();

        // Then
        assertThat(path.calcFare()).describedAs("1250 + 1000 + 100").isEqualTo(2350);
    }

    /**
     * When 어린이 멤버가 요금 요청시
     * Then 할인된 금액으로 요금이 계산이 된다
     */
    @DisplayName("멤버가 요금 요청시 할인된 금액으로 요금이 계산이 된다")
    @Test
    void calcFareForMemberWithChild() {
        // Given
        Sections sections = new Sections(Stream.of(이호선.getSections(), 신분당선.getSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Path path = new Path(sections);

        MemberResponse childMember = MemberResponse.of(new Member("child@naver.com", "test", 10));

        // When
        path.calcFareForMember(childMember);

        // Then
        assertThat(path.calcFare()).describedAs("(1250 + 1000 + 100 - 350) * 0.5 ").isEqualTo(1000);
    }

    /**
     * When 청소년 멤버가 요금 요청시
     * Then 할인된 금액으로 요금이 계산이 된다
     */
    @DisplayName("청소년 멤버가 요금 요청시 할인된 금액으로 요금이 계산이 된다")
    @Test
    void calcFareForMemberWithYouth() {
        // Given
        Sections sections = new Sections(Stream.of(이호선.getSections(), 신분당선.getSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Path path = new Path(sections);

        MemberResponse childMember = MemberResponse.of(new Member("yout@naver.com", "test", 16));

        // When
        path.calcFareForMember(childMember);

        // Then
        assertThat(path.calcFare()).describedAs("(1250 + 1000 + 100 - 350) * 0.8").isEqualTo(1600);
    }

    /**
     * When 경로에 총 거리 조회 요청 시
     * Then 조회가 된다
     */
    @DisplayName("경로에 총 거리 조회 요청 시 조회가 된다")
    @Test
    void extractDistance() {
        // Given
        Sections sections = new Sections(Stream.of(이호선.getSections(), 신분당선.getSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Path path = new Path(sections);

        // When
        int totalDistance = path.extractDistance();

        // Then
        assertThat(totalDistance).isEqualTo(15);
    }


    /**
     * When 경로에 총 시간 조회 요청 시
     * Then 조회가 된다
     */
    @DisplayName("경로에 총 거리 조회 요청 시 총 거리를 돌려준다")
    @Test
    void extractDuration() {
        // Given
        Sections sections = new Sections(Stream.of(이호선.getSections(), 신분당선.getSections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Path path = new Path(sections);

        // When
        int totalDuration = path.extractDuration();

        // Then
        assertThat(totalDuration).isEqualTo(15);
    }

}
