package nextstep.subway.line.domain;

import nextstep.subway.line.exception.CannotRemoveSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("노선 Domain 단위 테스트")
public class LineTest {

    private Station savedStationGyoDae;
    private Station savedStationGangNam;
    private Station savedStationYeokSam;
    private Station savedStationSeolleung;
    private Station savedStationSamSeong;
    private Line line2;

    @BeforeEach
    void setUp() {
        savedStationGangNam = new Station(1L, "강남역");
        savedStationYeokSam = new Station(2L, "역삼역");
        savedStationSamSeong = new Station(3L, "삼성역");
        savedStationGyoDae = new Station(4L, "교대역");
        savedStationSeolleung = new Station(5L, "선릉역");

        line2 = new Line(1L, "2호선", "bg-green-600");
        line2.addSection(savedStationGangNam, savedStationYeokSam, 10);
    }

    @Test
    @DisplayName("노선의 구간에 있는 역들을 가져오기")
    void getStations() {
        // when
        List<Station> stations = line2.getStations();

        // then
        assertThat(stations).hasSize(2);
    }

    // TODO : 보완
    @Disabled
    @Test
    @DisplayName("노선의 구간에 있는 역들을 가져오기1")
    void getStationsV1() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선에 신규 상행역 구간 추가. 교대 - 강남 - 역삼")
    void addSectionInUp() {
        // when
        line2.addSection(savedStationGyoDae, savedStationGangNam, 5);

        // then
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGyoDae, savedStationGangNam, savedStationYeokSam));
    }

    @Test
    @DisplayName("노선 중간에 신규 구간 추가. 강남 - 역삼 - 선릉 - 삼성")
    void addSectionInMiddle() {
        // given
        line2.addSection(savedStationYeokSam, savedStationSamSeong, 6);

        // when
        line2.addSection(savedStationYeokSam, savedStationSeolleung, 3);

        // then
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationYeokSam, savedStationSeolleung, savedStationSamSeong));
    }

    @Test
    @DisplayName("노선의 하행역에 신규 구간 추가. 강남 - 역삼 - 삼성")
    void addSectionInDown() {
        // when
        line2.addSection(savedStationYeokSam, savedStationSamSeong, 6);

        // then
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationYeokSam, savedStationSamSeong));
    }

    // TODO : 보완, addSectionInDown과 같은 테스트. Duration 보완
    @Disabled
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 10);

        assertThat(line.getStations().size()).isEqualTo(3);
    }

    // TODO : 보완, Duration
    @Disabled
    @DisplayName("목록 중간에 추가할 경우 - 소요 시간이 큰 경우 에러")
    @Test
    void addSectionInMiddleError() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 10);

        assertThatThrownBy(() -> line.addSection(강남역, 삼성역, 15, 15)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("노선에 존재하는 구간 추가 시 에러 발생")
    void addSectionAlreadyIncluded() {
        assertThatExceptionOfType(StationAlreadyExistException.class)
                .isThrownBy(() -> line2.addSection(savedStationGangNam, savedStationYeokSam, 10));
    }

    @Test
    @DisplayName("노선에 있는 상행 종점역 구간 제거")
    void removeUpStationSection() {
        // given
        line2.addSection(savedStationYeokSam, savedStationSamSeong, 6);

        // when
        line2.removeSection(savedStationGangNam);

        // then
        assertThat(line2.getSections()).hasSize(1);
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationYeokSam, savedStationSamSeong));
    }

    @Test
    @DisplayName("노선에 있는 중간 구간 제거")
    void removeMiddleSection() {
        // given
        line2.addSection(savedStationYeokSam, savedStationSamSeong, 6);
        line2.addSection(savedStationYeokSam, savedStationSeolleung, 3);

        // when
        line2.removeSection(savedStationYeokSam);

        // then
        assertThat(line2.getSections()).hasSize(2);
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationSeolleung, savedStationSamSeong));
        assertThat(line2.findSection(savedStationGangNam, savedStationSeolleung).getDistance()).isEqualTo(13);
    }

    @Test
    @DisplayName("노선에 있는 하행 종점역 구간 제거")
    void removeDownStationSection() {
        // given
        line2.addSection(savedStationYeokSam, savedStationSamSeong, 6);

        // when
        line2.removeSection(savedStationSamSeong);

        // then
        assertThat(line2.getSections()).hasSize(1);
        assertThat(line2.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationYeokSam));
    }

    @Test
    @DisplayName("구간이 하나인 노선 삭제 시 에러 발생")
    void removeSectionNotEndOfList() {
        assertThatExceptionOfType(CannotRemoveSectionException.class)
                .isThrownBy(() -> line2.removeSection(savedStationYeokSam));
    }
}
