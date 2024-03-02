package nextstep.subway.unit;

import nextstep.line.domain.Color;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private static List<String> 이호선_역 = List.of(
            "시청", "을지로입구", "을지로3가", "을지로4가", "동대문역사문화공원",
            "신당", "상왕십리", "왕십리", "한양대", "뚝섬", "성수", "건대입구", "구의", "강변");
    private static Map<String, Station> stationStore = new HashMap<>();


    @BeforeEach
    void setUp() {
        for (String station : 이호선_역) {
            stationStore.put(station, new Station(station));
        }
    }

    @Test
    void addSection() {
        Station 건대입구 = stationStore.get("건대입구");
        Station 구의 = stationStore.get("구의");
        Station 강변 = stationStore.get("강변");

        Line line = new Line("2호선", Color.GREEN, 건대입구, 구의, 6, 10);
        line.addSection(new Section(구의, 강변, 4, 10, line));

        assertThat(line.getStations()).containsExactly(건대입구, 구의, 강변);
    }

    @Test
    void getStations() {
        Station 건대입구 = stationStore.get("건대입구");
        Station 구의 = stationStore.get("구의");
        Station 강변 = stationStore.get("강변");

        Line line = new Line("2호선", Color.GREEN, 건대입구, 구의, 6, 10);
        Section section2 = new Section(구의, 강변, 4, 10, line);
        line.addSection(section2);

        assertThat(line.getStations()).containsExactly(건대입구, 구의, 강변);
    }


    @Test
    void removeMidStation() {
        Station 건대입구 = stationStore.get("건대입구");
        Station 구의 = stationStore.get("구의");
        Station 강변 = stationStore.get("강변");

        Line line = new Line("2호선", Color.GREEN, 건대입구, 구의, 6, 10);

        // given
        Section section2 = new Section(구의, 강변, 4, 10, line);
        line.addSection(section2);

        // when
        line.removeStation(구의);

        // then
        assertThat(line.getStations()).containsExactly(건대입구, 강변);
    }

    @Test
    void removeFirstStation() {
        Station 건대입구 = stationStore.get("건대입구");
        Station 구의 = stationStore.get("구의");
        Station 강변 = stationStore.get("강변");

        Line line = new Line("2호선", Color.GREEN, 건대입구, 구의, 6, 10);

        // given
        Section section2 = new Section(구의, 강변, 4, 10, line);
        line.addSection(section2);

        // when
        line.removeStation(건대입구);

        // then
        assertThat(line.getStations()).containsExactly(구의, 강변);
    }

    @Test
    void removeLastStation() {
        Station 건대입구 = stationStore.get("건대입구");
        Station 구의 = stationStore.get("구의");
        Station 강변 = stationStore.get("강변");

        Line line = new Line("2호선", Color.GREEN, 건대입구, 구의, 6, 10);

        // given
        Section section2 = new Section(구의, 강변, 4, 10, line);
        line.addSection(section2);

        // when
        line.removeStation(강변);

        // then
        assertThat(line.getStations()).containsExactly(건대입구, 구의);
    }



}
