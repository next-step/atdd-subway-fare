package nextstep.subway.maps.map.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathSectionsTest {

    private List<LineStationEdge> lineStationEdges;
    private SubwayPathSections subwayPathSections;

    @BeforeEach
    void setUp() {
        SubwayPathTestFixture fixture = new SubwayPathTestFixture();
        // new LineStationEdge(fixture.lineStation1, fixture.line1)
        lineStationEdges = Arrays.asList(
                new LineStationEdge(fixture.lineStation2, fixture.line1),
                new LineStationEdge(fixture.lineStation4, fixture.line2),
                new LineStationEdge(fixture.lineStation6, fixture.line3)
        );

        // given
        subwayPathSections = SubwayPathSections.from(lineStationEdges);

    }

    @DisplayName("출발역의 ID를 가져오는 테스트")
    @Test
    void getSourceStationId() {
        // when
        Long sourceStationId = subwayPathSections.getSourceStationId();

        // then
        assertThat(sourceStationId).isEqualTo(1L);
    }

    @DisplayName("도착역의 ID를 가져오는 테스트")
    @Test
    void getTargetStationId() {
        // when
        Long sourceStationId = subwayPathSections.getTargetStationId();

        // then
        // 한바퀴 돌아서 옴
        assertThat(sourceStationId).isEqualTo(4L);
    }


    @DisplayName("총 도착시간을 계산한다.")
    @Test
    void getAlightTime() {
        // given
        LocalTime time = LocalTime.of(11, 22);
        LocalTime expected = LocalTime.of(11, 52);

        // when
        LocalTime alightTime = subwayPathSections.getAlightTime(time);

        // then
        /*
         * 현재 시간 : 11시 22분.
         * line1 : 첫 차 시간 5:30, 10분 간격
         * 가장 빠른 열차 탑승: 11시 30분
         * 다음역까지 소요시간 2분 : 11시 32분
         * line2 : 첫 차 시간 5:30, 10분 간격
         * 가장 빠른 열차 탑승: 11시 40분
         * 다음역까지 소요시간 1분 : 11시 41분
         * line3 : 첫 차 시간 5:30, 10분 간격
         * 가장 빠른 열차 탑승: 11시 50분
         * 목적지까지 2분 : 11시 52분
         *
         * 최종 도착시간 11시 52분
         */
        assertThat(alightTime).isEqualTo(expected);
    }
}