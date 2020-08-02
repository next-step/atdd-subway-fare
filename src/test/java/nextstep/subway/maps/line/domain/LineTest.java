package nextstep.subway.maps.line.domain;

import com.google.common.collect.Lists;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    private Line line1;
    private Line line2;
    private Line line3;

    @BeforeEach
    void setUp() {
        Station station1 = new Station("강남역");
        Station station2 = new Station("교대역");
        Station station3 = new Station("양재역");
        Station station4 = new Station("남부터미널역");
        stationRepository.saveAll(Lists.newArrayList(station1, station2, station3, station4));

        LocalTime startTime = LocalTime.of(5, 30);
        LocalTime endTime = LocalTime.of(22, 30);
        line1 = new Line("신분당선", "red lighten-1", startTime, endTime, 3);
        line2 = new Line("2호선", "green lighten-1", startTime, endTime, 10);
        line3 = new Line("3호선", "orange darken-1", startTime, endTime, 5);

        line1.addLineStation(new LineStation(1L, null, 0, 0));
        line1.addLineStation(new LineStation(3L, 1L, 2, 2));

        line2.addLineStation(new LineStation(2L, null, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 2, 1));

        line3.addLineStation(new LineStation(2L, null, 0, 0));
        line3.addLineStation(new LineStation(4L, 2L, 1, 2));
        line3.addLineStation(new LineStation(3L, 4L, 2, 2));

        lineRepository.saveAll(Lists.newArrayList(line1, line2, line3));
    }

    @Test
    @DisplayName("지하철 노선에 추가 요금 값이 존재한다.")
    void hasExtraFare() {
        //given
        Line line = new Line("9호선", "BLUE_GREEN", LocalTime.now(), LocalTime.now(), 3, 300);
        Line savedLine = lineRepository.save(line);

        //when
        Money extraFare = savedLine.getExtraFare();

        //then
        assertThat(extraFare).isNotNull();
    }

    @Test
    @DisplayName("지하철 역과 시간이 주어지면 다음 열차시간을 계산한다.")
    void calculateNextTime() {
        //when
        LocalTime nextTime = line3.calculateNextDepartureTime(4L, LocalTime.of(6, 17));

        //then
        assertThat(nextTime).isEqualTo(LocalTime.of(6, 17));
    }

}