package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(readOnly = true)
@SpringBootTest
@DisplayName("경로 서비스(PathServiceTest)")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private EntityManager entityManager;

    private Station 강남역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        entityManager.persist(강남역);
        entityManager.persist(교대역);
        entityManager.persist(남부터미널역);
        entityManager.persist(양재역);

        // Duration == 100 - distance 형태로 값을 채움
        final Line 이호선 = new Line("2호선", "green");
        이호선.addSection(교대역, 강남역, 10, 90);
        entityManager.persist(이호선);

        final Line 신분당선 = new Line("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 10, 90);
        entityManager.persist(신분당선);

        final Line 삼호선 = new Line("삼호선", "orange");
        삼호선.addSection(교대역, 남부터미널역, 2, 98);
        삼호선.addSection(남부터미널역, 양재역, 3, 97); // 중복 제거 불가 -> args 로 메서드 분리 가능하나 혼란 야기할 듯
        entityManager.persist(삼호선);
    }

    @DisplayName("최단 거리를 반환한다.")
    @Test
    void findPathByShortestDistance() {
        final PathResponse pathResponse = pathService.findPathByShortestDistance(교대역.getId(), 양재역.getId());
        final int actual = pathResponse.getDistance();

        assertThat(actual).isEqualTo(5);
    }

    @DisplayName("최소 소요 시간을 반환한다.")
    @Test
    void findPathByShortestDuration() {
        final PathResponse pathResponse = pathService.findPathByShortestDuration(강남역.getId(), 양재역.getId());
        final int actual = pathResponse.getDistance();

        assertThat(actual).isEqualTo(180);
    }
}
