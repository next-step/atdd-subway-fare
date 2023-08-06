package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PathTest {

    private Section 중계_노원;
    private Section 노원_마들;
    private Section 마들_수락산;

    @BeforeEach
    void setUp() {
        Line 칠호선 = new Line();
        중계_노원 = new Section(칠호선, new Station("중계역"), new Station("노원역"), 10, 10);
        노원_마들 = new Section(칠호선, new Station("노원역"), new Station("마들역"), 6, 6);
        마들_수락산 = new Section(칠호선, new Station("마들역"), new Station("수락산역"), 42, 42);
    }

    @Test
    void 거리_10km_이하_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원)));

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(1250);
    }

    @Test
    void 거리_10km_초과_50km_까지_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원, 노원_마들)));

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(1450);
    }

    @Test
    void 거리_50km_초과_요금_계산() {
        //when
        Path path = new Path(new Sections(List.of(중계_노원, 노원_마들, 마들_수락산)));

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(2150);
    }
}
