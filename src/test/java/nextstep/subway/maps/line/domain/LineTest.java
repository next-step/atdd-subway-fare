package nextstep.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.domain.StationRepository;

@DataJpaTest
public class LineTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private Line 신분당선;
    private Line 서울_지하철_2호선;
    private Line 서울_지하철_3호선;

    @BeforeEach
    void setUp() {
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");
        stationRepository.saveAll(Arrays.asList(
            강남역, 교대역, 양재역, 남부터미널역
        ));

        LocalTime startTime = LocalTime.of(5, 30);
        LocalTime endTime = LocalTime.of(22, 30);

        신분당선 = new Line("신분당선", "red lighten-1", startTime, endTime, 3);
        서울_지하철_2호선 = new Line("2호선", "green lighten-1", startTime, endTime, 10);
        서울_지하철_3호선 = new Line("3호선", "orange darken-1", startTime, endTime, 5);

        LineStation 신분당선_강남역 = new LineStation(1L, null, 0, 0);
        LineStation 신분당선_양재역 = new LineStation(3L, 1L, 2, 2);
        신분당선.addLineStation(신분당선_강남역);
        신분당선.addLineStation(신분당선_양재역);

        LineStation 서울_지하철_2호선_교대역 = new LineStation(2L, null, 0, 0);
        LineStation 서울_지하철_2호선_강남역 = new LineStation(1L, 2L, 2, 1);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_교대역);
        서울_지하철_2호선.addLineStation(서울_지하철_2호선_강남역);

        LineStation 서울_지하철_3호선_교대역 = new LineStation(2L, null, 0, 0);
        LineStation 서울_지하철_3호선_남부터미널역 = new LineStation(4L, 2L, 1, 2);
        LineStation 서울_지하철_3호선_양재역 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_교대역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_남부터미널역);
        서울_지하철_3호선.addLineStation(서울_지하철_3호선_양재역);

        lineRepository.saveAll(Lists.newArrayList(
            신분당선, 서울_지하철_2호선, 서울_지하철_3호선
        ));
    }

    @DisplayName("지하철 노선에는 추가 요금 필드가 존재한다.")
    @Test
    void 지하철_노선에_추가요금이_존재한다() {
        // given
        Line line = new Line("인천1호선", "COLD_BLUE", LocalTime.now(), LocalTime.now(), 3, 400);
        Line savedLine = lineRepository.save(line);

        // when
        assertThat(savedLine.getExtraFare()).isNotNull();
    }

    @DisplayName("지하철 역과 시간이 주어지면 다음 열차도착 시간을 계산한다.")
    @Test
    void 다음_열차도착시간을_계산한다() {
        // when
        LocalTime nextTime = 서울_지하철_3호선.calculateNextDepartureTime(4L, LocalTime.of(6, 17));

        // then
        assertThat(nextTime).isEqualTo(LocalTime.of(6, 17));
    }
}
