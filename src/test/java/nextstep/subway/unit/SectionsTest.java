package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {

    Line line;
    Station upStation;
    Station downStation;
    Section originSection;
    Sections sections;

    @BeforeEach
    void setUp() {
        line = new Line("2호선", "green");
        upStation = createStation(1L, "강남역");
        downStation = createStation(2L, "건대입구역");
        originSection = new Section(line, upStation, downStation, 10, 5);
        sections = new Sections();
    }

    @DisplayName("구간을 성공적으로 추가한다")
    @Test
    public void add_section_test() {
        // when
        sections.add(originSection);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, downStation);
    }

    @DisplayName("노선의 상행역과 신규 추가할 구간의 하행역이 동일할 경우 노선의 앞부분에 신규 구간을 추가할 수 있다.")
    @Test
    public void add_section_front_at_line() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");

        // when
        Section newSection = new Section(line, newStation, upStation, 5, 3);
        sections.add(newSection);

        // then
        assertThat(sections.getStations()).containsExactly(newStation, upStation, downStation);
    }

    @DisplayName("구간을 맨 앞에 추가한 후, 동일한 상행역을 통해 중간에 추가할 수 있다")
    @Test
    public void add_section_front_and_middle() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");

        Section newSection = new Section(line, newStation, upStation, 5, 3);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, upStation, newStation2, 1, 1);

        // when
        sections.add(newSection2);

        // then
        assertThat(sections.getStations()).containsExactly(newStation, upStation, newStation2, downStation);
    }

    @DisplayName("구간을 맨 앞에 추가한 후, 동일한 하행역을 통해 중간에 추가할 수 있다")
    @Test
    public void add_section_front_and_middle_2() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, newStation, upStation, 5, 3);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, newStation2, downStation, 1, 1);

        // when
        sections.add(newSection2);

        // then
        assertThat(sections.getStations()).containsExactly(newStation, upStation, newStation2, downStation);
    }

    @DisplayName("상행역 앞에 하나, 상행역 바로 뒤에 두번의 역을 추가한다")
    @Test
    public void add_section_middle_3() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, newStation, upStation, 5, 3);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, upStation, newStation2, 6, 4);
        sections.add(newSection2);

        Station newStation3 = createStation(5L, "신규역3");
        Section newSection3 = new Section(line, upStation, newStation3, 2, 2);

        // when
        sections.add(newSection3);

        // then
        assertThat(sections.getStations()).containsExactly(newStation, upStation, newStation3, newStation2, downStation);
    }

    @DisplayName("상행역 앞에 하나, 상행역 바로 뒤에 한번, 하행역 바로 앞에 한번 추가한다")
    @Test
    public void add_section_middle_4() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, newStation, upStation, 5, 5);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, upStation, newStation2, 6, 6);
        sections.add(newSection2);

        Station newStation3 = createStation(5L, "신규역3");
        Section newSection3 = new Section(line, newStation3, downStation, 2, 2);

        // when
        sections.add(newSection3);

        // then
        assertThat(sections.getStations()).containsExactly(newStation, upStation, newStation2, newStation3, downStation);
    }

    @DisplayName("신규역을 상행 구간 앞으로 3번 추가할 수 있다")
    @Test
    public void add_section_front_3_times() {
        // given
        sections.add(originSection);
        Station newStation = createStation(3L, "신규역");

        // when
        Section newSection = new Section(line, newStation, upStation, 8, 8);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, newStation, newStation2, 4, 4);
        sections.add(newSection2);

        Station newStation3 = createStation(5L, "신규역3");
        Section newSection3 = new Section(line, newStation2, newStation3, 2, 2);
        sections.add(newSection3);

        //then
        assertThat(sections.getStations()).containsExactly(newStation, newStation2, newStation3, upStation, downStation);
    }

    @DisplayName("노선의 하행역과 신규 추가할 구간의 상행역이 동일할 경우 노선의 뒷부분에 신규 구간을 추가할 수 있다.")
    @Test
    public void add_section_back_at_line() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");

        // when
        Section newSection = new Section(line, downStation, newStation, 5, 5);
        sections.add(newSection);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, downStation, newStation);
    }

    @DisplayName("신규 구간의 상행역이 기존 구간의 상행역과 동일할 경우 중간에 구간을 추가할 수 있다")
    @Test
    public void add_section_at_middle_of_line_when_same_up_station() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");

        // when
        Section newSection = new Section(line, upStation, newStation, 3, 3);
        sections.add(newSection);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, newStation, downStation);
    }

    @DisplayName("신규 구간의 상행역이 기존 구간의 상행역과 동일할 경우 중간에 구간을 추가할 수 있다")
    @Test
    public void add_section_at_middle_of_line_when_same_down_station() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");

        // when
        Section newSection = new Section(line, newStation, downStation, 3, 3);
        sections.add(newSection);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, newStation, downStation);
    }

    @DisplayName("역 사이에 새로운 역 추가할때, 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다")
    @Test
    public void add_section_fail_by_already_register_up_down_stations() {
        // given
        sections.add(originSection);

        // when
        Section newSection = new Section(line, upStation, downStation, 3, 3);
        ThrowableAssert.ThrowingCallable actual = () -> sections.add(newSection);

        // then
        assertThatThrownBy(actual)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Sections에 등록된 모든 역을 조회한다")
    @Test
    public void get_all_station_in_section() {
        // given
        sections.add(originSection);

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(upStation, downStation);
    }

    @DisplayName("Sections의 첫 section을 삭제할 수 있다")
    @Test
    public void delete_first_section() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, downStation, newStation, 5, 5);
        sections.add(newSection);

        // when
        sections.delete(upStation);

        // then
        assertThat(sections.getStations()).containsExactly(downStation, newStation);
    }

    @DisplayName("Sections의 마지막 section을 삭제할 수 있다")
    @Test
    public void delete_last_section() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, downStation, newStation, 5, 5);
        sections.add(newSection);

        // when
        sections.delete(newStation);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, downStation);
    }

    @DisplayName("연속된 두 구간의 중간역을 삭제할 수 있다")
    @Test
    public void delete_middle_station() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, downStation, newStation, 5, 5);
        sections.add(newSection);

        // when
        sections.delete(downStation);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, newStation);
    }

    @DisplayName("연속된 3 구간의 중간역을 삭제할 수 있다")
    @Test
    public void delete_middle_station_2() {
        // given
        sections.add(originSection);

        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, downStation, newStation, 5, 5);
        sections.add(newSection);

        Station newStation2 = createStation(4L, "신규역2");
        Section newSection2 = new Section(line, newStation, newStation2, 9, 9);
        sections.add(newSection2);

        // when
        sections.delete(newStation);

        // then
        assertThat(sections.getStations()).containsExactly(upStation, downStation, newStation2);
    }

    @DisplayName("구간이 하나 남았을때 삭제할 경우 예외가 발생한다.")
    @Test
    public void cant_delete_section_when_only_one_section() {
        // given
        sections.add(originSection);

        //when
        ThrowableAssert.ThrowingCallable actual = () -> sections.delete(upStation);

        // then
        assertThatThrownBy(actual)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("동일한 상행역을 갖는 신규 구간을 중간에 삽입시 소요 시간 분리")
    @Test
    public void same_up_station_rearrange_duration() {
        // given
        sections.add(originSection);
        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, upStation, newStation, 3, 3);

        // when
        sections.add(newSection);

        // then
        List<Section> sections = this.sections.getSections();
        assertThat(sections.get(0).getDuration()).isEqualTo(2);
        assertThat(sections.get(1).getDuration()).isEqualTo(3);
    }

    @DisplayName("동일한 하행역을 갖는 신규 구간을 중간에 삽입시 소요 시간 분리")
    @Test
    public void same_down_station_rearrange_duration() {
        // given
        sections.add(originSection);
        Station newStation = createStation(3L, "신규역");
        Section newSection = new Section(line, newStation, downStation, 3, 3);

        // when
        sections.add(newSection);

        // then
        List<Section> sections = this.sections.getSections();
        assertThat(sections.get(0).getDuration()).isEqualTo(2);
        assertThat(sections.get(1).getDuration()).isEqualTo(3);
    }

    private Station createStation(Long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}
