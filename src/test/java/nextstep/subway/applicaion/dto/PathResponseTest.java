package nextstep.subway.applicaion.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathResponseTest {

    private static final int 기본_요금 = 1250;

    @Test
    void 이용_거리가_0이면_요금도_0원이다() {
        PathResponse response = new PathResponse(List.of(), 0, 0);

        assertThat(response.getFare()).isZero();
    }

    @Test
    void 이용_거리가_10km이하는_기본_요금이_나와야_한다() {
        PathResponse response1 = new PathResponse(List.of(), 8, 3);
        PathResponse response2 = new PathResponse(List.of(), 10, 3);

        assertThat(response1.getFare()).isEqualTo(기본_요금);
        assertThat(response2.getFare()).isEqualTo(기본_요금);
    }

    /**
     * 11km = 10km + 1km = 기본_요금 + ceil(1/5)*100
     * 37km = 10km + 27km = 기본_요금 + ceil(27/5)*100
     * 50km = 10km + 40km = 기본_요금 + ceil(40/5)*100
     */
    @Test
    void 이용_거리가_10km초과_50km이하는_5km마다_100원의_추가요금이_나와야_한다() {
        PathResponse response1 = new PathResponse(List.of(), 11, 3);
        PathResponse response2 = new PathResponse(List.of(), 37, 3);
        PathResponse response3 = new PathResponse(List.of(), 50, 3);

        assertThat(response1.getFare()).isEqualTo(기본_요금 + 100);
        assertThat(response2.getFare()).isEqualTo(기본_요금 + 600);
        assertThat(response3.getFare()).isEqualTo(기본_요금 + 800);
    }

    /**
     * 51km = 10km + 40km + 1km = 기본_요금 + 8*100 + ceil(1/8)*100
     * 107km = 10km + 40km + 56km = 기본_요금 + 8*100 + ceil(57/8)*100
     */
    @Test
    void 이용_거리가_50km초과분은_8km마다_100원의_추가요금이_나와야_한다() {
        PathResponse response1 = new PathResponse(List.of(), 51, 3);
        PathResponse response2 = new PathResponse(List.of(), 107, 3);

        assertThat(response1.getFare()).isEqualTo(기본_요금 + 800 + 100);
        assertThat(response2.getFare()).isEqualTo(기본_요금 + 800 + 800);
    }
}