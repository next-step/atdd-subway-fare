package nextstep.line.application;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.exception.*;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.line.LineTestField.*;
import static nextstep.line.domain.path.ShortPathType.DISTANCE;
import static nextstep.line.domain.path.ShortPathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Station 수원역 = new Station("수원역");
    private static final Station 노원역 = new Station("노원역");
    private static final Station 대림역 = new Station("대림역");

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private LineService lineService;

    @DisplayName("지하철 노선에 구간을 추가하면 노선 역이름 조회시 추가되있어야 한다.")
    @Test
    void 구간추가() {
        // given
        when(stationRepository.findStation(2L)).thenReturn(선릉역);
        when(stationRepository.findStation(3L)).thenReturn(수원역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        // when
        lineService.addSection(1L, new SectionAddRequest(2L, 3L, 3, 4));

        // then
        // lineService.findLineById 메서드를 통해 검증
        assertThat(lineService.findLine(1L).getStations()).hasSize(3)
                .extracting("name")
                .containsExactly("강남역", "선릉역", "수원역");
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재할 경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두존재() {
        // given
        when(stationRepository.findStation(1L)).thenReturn(강남역);
        when(stationRepository.findStation(2L)).thenReturn(선릉역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        // when then
        assertThatThrownBy(() -> lineService.addSection(1L, new SectionAddRequest(1L, 2L, 3, 4)))
                .isExactlyInstanceOf(SectionExistException.class)
                .hasMessage("구간 상행역, 하행역이 이미 노선에 등록되어 있습니다.");
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재하지 않을경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두미존재() {
        // given
        when(stationRepository.findStation(1L)).thenReturn(수원역);
        when(stationRepository.findStation(2L)).thenReturn(노원역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        // when then
        assertThatThrownBy(() -> lineService.addSection(1L, new SectionAddRequest(2L, 1L, 3, 4)))
                .isExactlyInstanceOf(SectionNotExistException.class)
                .hasMessage("구간 상행역, 하행역이 노선에 하나도 포함되어있지 않습니다.");
    }

    @DisplayName("지하철 노선에 구간시 기존구간에 길이를 초과하면 에러를 던진다.")
    @Test
    void 구간추가_기존구간_길이초과() {
        // given
        when(stationRepository.findStation(2L)).thenReturn(선릉역);
        when(stationRepository.findStation(3L)).thenReturn(수원역);
        when(stationRepository.findStation(4L)).thenReturn(노원역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        lineService.addSection(1L, new SectionAddRequest(2L, 3L, 3, 4));

        // when then
        assertThatThrownBy(() -> lineService.addSection(1L, new SectionAddRequest(2L, 4L, 5, 4)))
                .isExactlyInstanceOf(SectionDistanceOverException.class)
                .hasMessage("구간길이를 초과했습니다.");
    }

    @DisplayName("지하철 노선에 구간을 삭제하면 노선 역이름 조회시 삭제한 역은 제외되야 한다.")
    @Test
    void 구간삭제() {
        // given
        when(stationRepository.findStation(2L)).thenReturn(선릉역);
        when(stationRepository.findStation(3L)).thenReturn(수원역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        lineService.addSection(1L, new SectionAddRequest(2L, 3L, 3, 4));

        // when
        lineService.deleteSection(1L, 3L);

        // then
        assertThat(lineService.findLine(1L).getStations()).hasSize(2)
                .extracting("name")
                .containsExactly(
                        강남역.getName(),
                        선릉역.getName()
                );
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간정보가 1개이므로 삭제가 실패되어야 한다.")
    @Test
    void 구간삭제_구간최소갯수() {
        // given
        when(stationRepository.findStation(2L)).thenReturn(선릉역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5)
        ));

        // when then
        assertThatThrownBy(() -> lineService.deleteSection(1L, 2L))
                .isExactlyInstanceOf(SectionDeleteMinSizeException.class)
                .hasMessage("구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간에 포함된 역이 아닌경우 삭제에 실패되어야 한다.")
    @Test
    void 구간삭제_구간미포함_역() {
        // given
        when(stationRepository.findStation(2L)).thenReturn(선릉역);
        when(stationRepository.findStation(3L)).thenReturn(수원역);
        when(stationRepository.findStation(4L)).thenReturn(노원역);

        when(lineRepository.findById(1L)).thenReturn(Optional.of(
                new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 4)
        ));

        lineService.addSection(1L, new SectionAddRequest(2L, 3L, 3, 4));

        // when then
        assertThatThrownBy(() -> lineService.deleteSection(1L, 4L))
                .isExactlyInstanceOf(SectionNotFoundException.class)
                .hasMessage("구간정보를 찾을 수 없습니다.");
    }

    @Nested
    class ShortPath {

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 이동거리가 가장 짧은 경로를 리턴해야한다.")
        @Test
        void 강남역_수원역_이동거리_검증() {
            // given
            when(stationRepository.findStation(1L)).thenReturn(강남역);
            when(stationRepository.findStation(2L)).thenReturn(수원역);

            when(lineRepository.findAll()).thenReturn(List.of(
                    new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 3),
                    new Line(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 5),
                    new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 1),
                    new Line(FOUR_LINE_NAME, FOUR_LINE_COLOR, 노원역, 수원역, 3, 4)
            ));

            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 1L, 2L, new AnonymousPrincipal());

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 소요시간이 가장 짧은 경로를 리턴해야한다.")
        @Test
        void 강남역_수원역_소요시간_검증() {
            // given
            when(stationRepository.findStation(1L)).thenReturn(강남역);
            when(stationRepository.findStation(2L)).thenReturn(수원역);

            when(lineRepository.findAll()).thenReturn(List.of(
                    new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 3),
                    new Line(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 5),
                    new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 1),
                    new Line(FOUR_LINE_NAME, FOUR_LINE_COLOR, 노원역, 수원역, 3, 4)
            ));

            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DURATION, 1L, 2L, new AnonymousPrincipal());

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 노원역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDuration()).isEqualTo(5);
            assertThat(shortPathResponse.getDistance()).isEqualTo(8);
        }

        @DisplayName("최단경로 조회 역중 노선에 포함되지 않은 역이 존재할 경우 에러를 던진다.")
        @Test
        void 경로조회_미포함역() {
            // given
            when(stationRepository.findStation(1L)).thenReturn(선릉역);
            when(stationRepository.findStation(2L)).thenReturn(대림역);

            when(lineRepository.findAll()).thenReturn(List.of(
                    new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4),
                    new Line(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4),
                    new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4),
                    new Line(FOUR_LINE_NAME, FOUR_LINE_COLOR, 노원역, 수원역, 3, 4)
            ));

            // when then
            assertThatThrownBy(() -> lineService.findShortPath(DISTANCE, 1L, 2L, new AnonymousPrincipal()))
                    .isExactlyInstanceOf(StationNotExistException.class)
                    .hasMessage("노선에 역이 존재하지 않습니다.");
        }

        @DisplayName("최단경로 조회 시작역, 종착역이 동일할 경우 에러를 던진다.")
        @Test
        void 경로조회_시작역_종착역_동일() {
            // given
            when(stationRepository.findStation(1L)).thenReturn(대림역);

            when(lineRepository.findAll()).thenReturn(List.of(
                    new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4),
                    new Line(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4),
                    new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4),
                    new Line(FOUR_LINE_NAME, FOUR_LINE_COLOR, 노원역, 수원역, 3, 4)
            ));

            // given when then
            assertThatThrownBy(() -> lineService.findShortPath(DISTANCE, 1L, 1L, new AnonymousPrincipal()))
                    .isExactlyInstanceOf(ShortPathSameStationException.class)
                    .hasMessage("최단경로 시작역, 종착역이 동일할 수 없습니다.");
        }
    }
}
