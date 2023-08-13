package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PathTest {

    private Station 노원역;
    private Station 중계역;
    private Station 마들역;
    private Station 수락산역;
    private Station 상계역;
    private Section 중계_노원;
    private Section 노원_마들;
    private Section 마들_수락산;
    private Section 노원_상계;

    @BeforeEach
    void setUp() {
        노원역 = new Station("노원역");
        중계역 = new Station("중계역");
        마들역 = new Station("마들역");
        상계역 = new Station("상계역");
        수락산역 = new Station("수락산역");
        Line 칠호선 = new Line("칠호선", "dark-green", 100);
        중계_노원 = new Section(칠호선, 중계역, 노원역, 10, 10);
        노원_마들 = new Section(칠호선, 노원역, 마들역, 6, 6);
        마들_수락산 = new Section(칠호선, 마들역, 수락산역, 42, 42);

        Line 사호선 = new Line("사호선", "sky-blue", 500);
        노원_상계 = new Section(사호선, 노원역, 상계역, 10, 10);
    }

    @Test
    void 거리_10km_이하_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원)));

        //then
        Assertions.assertThat(path.getFee(null)).isEqualTo(1350);
    }

    @Test
    void 거리_10km_초과_50km_까지_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원, 노원_마들)));

        //then
        Assertions.assertThat(path.getFee(null)).isEqualTo(1550);
    }

    @Test
    void 거리_50km_초과_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원, 노원_마들, 마들_수락산)));

        //then
        Assertions.assertThat(path.getFee(null)).isEqualTo(2250);
    }

    @Test
    void 경로_중_추가요금이_있는_노선을_환승_하여_이용_할_경우_가장_높은_금액의_추가_요금만_적용() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원, 노원_상계)));

        //then
        Assertions.assertThat(path.getFee(null)).isEqualTo(1950);
    }

    @Test
    void 청소년_회원_거리_10km_이하_요금_계산() {
        //when
        Member member = new Member("teenager", "password", 18);
        Path path = new Path(new Sections(List.of(중계_노원)));

        //then
        Assertions.assertThat(path.getFee(member)).isEqualTo(800);
    }

    @Test
    void 어린이_회원_거리_10km_이하_요금_계산() {
        //when
        Member member = new Member("children", "password", 12);
        Path path = new Path(new Sections(List.of(중계_노원)));

        //then
        Assertions.assertThat(path.getFee(member)).isEqualTo(500);
    }

    @Test
    void 성인_회원_거리_10km_이하_요금_계산() {
        //when
        Member member = new Member("children", "password", 19);
        Path path = new Path(new Sections(List.of(중계_노원)));

        //then
        Assertions.assertThat(path.getFee(member)).isEqualTo(1350);
    }

}
