package nextstep.subway.unit;

import nextstep.line.domain.Color;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionTest {

    private static List<String> 이호선_역 = List.of(
            "시청", "을지로입구", "을지로3가", "을지로4가", "동대문역사문화공원",
            "신당", "상왕십리", "왕십리", "한양대", "뚝섬", "성수", "건대입구", "구의", "강변");
    private static final Map<String, Station> stationStore = new HashMap<>();


    @BeforeEach
    void setUp() {
        for (String station : 이호선_역) {
            stationStore.put(station, new Station(station));
        }
    }


    @DisplayName("구간을 하행역에 추가하면 노선의 Distance에 추가된 구간의 거리가 누적된다")
    @Test
    void appendSection() {
        // given
        Station 시청 = stationStore.get("시청");
        Station 을지로입구 = stationStore.get("을지로입구");
        Station 을지로3가 = stationStore.get("을지로3가");

        Line line = new Line("2호선", Color.GREEN, 시청, 을지로입구, 6, 10);

        // when
        line.addSection(new Section(을지로입구, 을지로3가, 4, 10, line));

        // then
        Assertions.assertThat(line.getDistance()).isEqualTo(10);
    }


    @DisplayName("구간 A -> C 에 A -> B를 추가하면 A -> B -> C의 거리는 A -> C와 동일하다")
    @Test
    void insertSectionDistance() {
        // given
        Station 시청 = stationStore.get("시청");
        Station 을지로3가 = stationStore.get("을지로3가");
        Line line = new Line("2호선", Color.GREEN, 시청, 을지로3가, 6, 10);

        Station 을지로입구 = stationStore.get("을지로입구");
        // when
        line.addSection(new Section(시청, 을지로입구, 4, 10, line));

        // then
        Assertions.assertThat(line.getDistance()).isEqualTo(6);
    }


    @DisplayName("A - C 구간에 A - B를 추가하면 A - B - C가 된다")
    @Test
    void insertSection() {

        // given
        Station 시청 = stationStore.get("시청");
        Station 강변 = stationStore.get("강변");
        Line line = new Line("2호선", Color.GREEN, 시청, 강변, 6, 10);

        // when
        Station 을지로입구 = stationStore.get("을지로입구");
        line.addSection(new Section(시청, 을지로입구, 4, 4, line));

        // then
        Assertions.assertThat(line.getStations())
                .containsExactly(시청, 을지로입구, 강변);
    }
}
