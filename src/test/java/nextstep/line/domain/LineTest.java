package nextstep.line.domain;

import nextstep.exception.*;
import nextstep.station.domain.Station;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.line.LineTestField.SHINBUNDANG_LINE_COLOR;
import static nextstep.line.LineTestField.SHINBUNDANG_LINE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Station 수원역 = new Station("수원역");
    private static final Station 노원역 = new Station("노원역");
    private static final Station 대림역 = new Station("대림역");

    @DisplayName("지하철 노선에 구간을 추가하면 노선 역이름 조회시 추가되있어야 한다.")
    @Test
    void 구간추가() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        // when
        line.addSection(선릉역, 수원역, 3,4);
        line.addSection(대림역, 강남역, 3,4);
        line.addSection(선릉역, 노원역, 2,4);

        // then
        assertThat(line.getStations())
                .containsExactly(대림역, 강남역, 선릉역, 노원역, 수원역);
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재할 경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두존재() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        // when then
        assertThatThrownBy(() -> line.addSection(강남역, 선릉역, 3,4))
                .isExactlyInstanceOf(SectionExistException.class)
                .hasMessage("구간 상행역, 하행역이 이미 노선에 등록되어 있습니다.");
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재하지 않을경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두미존재() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        // when then
        assertThatThrownBy(() -> line.addSection(노원역, 대림역, 3,4))
                .isExactlyInstanceOf(SectionNotExistException.class)
                .hasMessage("구간 상행역, 하행역이 노선에 하나도 포함되어있지 않습니다.");
    }

    @DisplayName("지하철 노선에 구간시 기존구간에 길이를 초과하면 에러를 던진다.")
    @Test
    void 구간추가_기존구간_길이초과() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        line.addSection(선릉역, 수원역, 3,4);
        line.addSection(대림역, 강남역, 3,4);

        // when then
        assertThatThrownBy(() -> line.addSection(노원역, 선릉역, 14,4))
                .isExactlyInstanceOf(SectionDistanceOverException.class)
                .hasMessage("구간길이를 초과했습니다.");
    }

    @DisplayName("지하철 노선에 등록된 역을 조회하면 지금까지 등록된 모든 역에 정보가 조회되야 한다.")
    @Test
    void 노선_역_조회() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        line.addSection(선릉역, 수원역, 3,4);
        line.addSection(대림역, 강남역, 3,4);
        line.addSection(노원역, 선릉역, 1,4);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).hasSize(5);
        assertThat(stations).containsExactly(대림역, 강남역, 노원역, 선릉역, 수원역);
    }

    @DisplayName("지하철 노선에 등록된 구간을 조회하면 지금까지 등록된 모든 구간에 정보가 조회되야 한다.")
    @Test
    void 노선_구간_조회() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        line.addSection(대림역, 강남역, 7,4);
        line.addSection(선릉역, 수원역, 5,4);
        line.addSection(노원역, 선릉역, 4,4);

        // when
        List<Section> sections = line.getSections();

        // then
        assertThat(sections)
                .hasSize(4)
                .extracting("upStation", "downStation", "distance")
                .containsExactly(
                        Tuple.tuple(대림역, 강남역, 7),
                        Tuple.tuple(강남역, 노원역, 6),
                        Tuple.tuple(노원역, 선릉역, 4),
                        Tuple.tuple(선릉역, 수원역, 5)
                );
    }

    @DisplayName("지하철 노선에 구간을 삭제하면 노선 역이름 조회시 삭제한 역은 제외되야 한다.")
    @Test
    void 구간삭제() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);
        line.addSection(선릉역, 수원역, 3,4);
        line.addSection(수원역, 노원역, 4,4);

        // when
        line.removeSection(노원역);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 선릉역, 수원역);
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간정보가 1개이므로 삭제가 실패되어야 한다.")
    @Test
    void 구간삭제_구간최소갯수() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);

        // when then
        assertThatThrownBy(() -> line.removeSection(선릉역))
                .isExactlyInstanceOf(SectionDeleteMinSizeException.class)
                .hasMessage("구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간에 포함된 역이 아닌경우 삭제에 실패되어야 한다.")
    @Test
    void 구간삭제_구간미포함_역() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);
        line.addSection(선릉역, 수원역, 8,4);

        // when then
        assertThatThrownBy(() -> line.removeSection(노원역))
                .isExactlyInstanceOf(SectionNotFoundException.class)
                .hasMessage("구간정보를 찾을 수 없습니다.");
    }

    @DisplayName("지하철 노선 추가 및 삭제 후 지하철 노선에 등록된 구간을 조회하면 지금까지 등록된 모든 구간에 정보가 조회되야 한다.")
    @Test
    void 구간삭제_후_노선구간_조회() {
        // given
        Line line = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4);
        line.addSection(선릉역, 수원역, 8,4);
        line.addSection(노원역, 수원역, 4,4);
        line.removeSection(노원역);
        // when
        List<Section> sections = line.getSections();

        // then
        assertThat(sections)
                .hasSize(2)
                .extracting("upStation", "downStation", "distance")
                .containsExactly(
                        Tuple.tuple(강남역, 선릉역, 10),
                        Tuple.tuple(선릉역, 수원역, 8)
                );
    }
}
