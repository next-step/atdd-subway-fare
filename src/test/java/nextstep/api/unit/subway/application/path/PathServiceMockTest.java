package nextstep.api.unit.subway.application.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import static nextstep.api.unit.subway.StationFixture.강남역;
import static nextstep.api.unit.subway.StationFixture.교대역;
import static nextstep.api.unit.subway.StationFixture.선릉역;
import static nextstep.api.unit.subway.StationFixture.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.api.SubwayException;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.line.LineRepository;
import nextstep.api.subway.domain.path.PathSelection;
import nextstep.api.subway.domain.station.Station;
import nextstep.api.subway.domain.station.StationRepository;
import nextstep.api.subway.domain.station.exception.NoSuchStationException;
import nextstep.api.unit.subway.LineFixture;

@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private PathService pathService;

    private static final Long SOURCE_ID = 1L;
    private static final Long TARGET_ID = 2L;

    @DisplayName("최단경로를 조회한다")
    @Nested
    class FindShortestPathTest {

        @Nested
        class Success {

            @Test
            void 거리기준_최단경로를_조회한다() {
                // given
                given(stationRepository.getById(SOURCE_ID)).willReturn(교대역);
                given(stationRepository.getById(TARGET_ID)).willReturn(선릉역);
                given(stationRepository.findAll()).willReturn(
                        List.of(교대역, 강남역, 역삼역, 선릉역));
                given(lineRepository.findAll()).willReturn(List.of(
                        LineFixture.makeLine(
                                교대역, 강남역, 역삼역, 선릉역),
                        LineFixture.makeLine(교대역, 역삼역, 1, 1)
                ));

                // when
                final var response = pathService.findShortestPath(SOURCE_ID, TARGET_ID, PathSelection.DISTANCE.name());

                //then
                final var stations = response.getStations().stream()
                        .map(StationResponse::getId).collect(Collectors.toUnmodifiableList());
                final var expected = Stream.of(교대역, 역삼역, 선릉역)
                        .map(Station::getId).collect(Collectors.toUnmodifiableList());
                assertThat(stations).isEqualTo(expected);
            }

            @Test
            void 소요시간기준_최단경로를_조회한다() {
                // given
                given(stationRepository.getById(SOURCE_ID)).willReturn(교대역);
                given(stationRepository.getById(TARGET_ID)).willReturn(선릉역);
                given(stationRepository.findAll()).willReturn(List.of(교대역, 강남역, 역삼역, 선릉역));
                given(lineRepository.findAll()).willReturn(List.of(
                        LineFixture.makeLine(교대역, 강남역, 역삼역, 선릉역),
                        LineFixture.makeLine(교대역, 역삼역, 1, 1)
                ));

                // when
                final var response = pathService.findShortestPath(SOURCE_ID, TARGET_ID, PathSelection.DURATION.name());

                //then
                final var stations = response.getStations().stream()
                        .map(StationResponse::getId).collect(Collectors.toUnmodifiableList());
                final var expected = Stream.of(교대역, 역삼역, 선릉역)
                        .map(Station::getId).collect(Collectors.toUnmodifiableList());
                assertThat(stations).isEqualTo(expected);
            }
        }

        @Nested
        class Fail {

            @Test
            void 출발역과_도착역이_동일한_경우() {
                assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L, PathSelection.DISTANCE.name()))
                        .isInstanceOf(SubwayException.class);
            }

            @Test
            void 출발역에서_도착역까지의_경로가_이어지지_않은_경우() {
                // given
                given(stationRepository.getById(SOURCE_ID)).willReturn(교대역);
                given(stationRepository.getById(TARGET_ID)).willReturn(선릉역);
                given(stationRepository.findAll()).willReturn(
                        List.of(교대역, 강남역, 역삼역, 선릉역));
                given(lineRepository.findAll()).willReturn(List.of(
                        LineFixture.makeLine(교대역, 강남역, 역삼역)));

                // when
                assertThatThrownBy(
                        () -> pathService.findShortestPath(SOURCE_ID, TARGET_ID, PathSelection.DISTANCE.name()))
                        .isInstanceOf(SubwayException.class);
            }

            @Test
            void 출발역이_존재하지_않는_경우() {
                // given
                given(stationRepository.getById(SOURCE_ID)).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(
                        () -> pathService.findShortestPath(SOURCE_ID, TARGET_ID, PathSelection.DISTANCE.name()))
                        .isInstanceOf(NoSuchStationException.class);
            }

            @Test
            void 도착역이_존재하지_않는_경우() {
                // given
                given(stationRepository.getById(SOURCE_ID)).willReturn(역삼역);
                given(stationRepository.getById(TARGET_ID)).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(
                        () -> pathService.findShortestPath(SOURCE_ID, TARGET_ID, PathSelection.DISTANCE.name()))
                        .isInstanceOf(NoSuchStationException.class);
            }
        }
    }
}
