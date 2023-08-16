package nextstep.line.application;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.exception.*;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.application.response.LineResponse;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.line.LineTestField.*;
import static nextstep.line.domain.path.ShortPathType.DISTANCE;
import static nextstep.line.domain.path.ShortPathType.DURATION;
import static nextstep.member.MemberTestUser.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class LineServiceTest {

    private Station 강남역;
    private Station 선릉역;
    private Station 수원역;
    private Station 노원역;
    private Station 대림역;
    private Station 파람역;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        강남역 = saveStation(GANGNAM_STATION_NAME);
        선릉역 = saveStation(SEOLLEUNG_STATION_NAME);
        수원역 = saveStation(SUWON_STATION_NAME);
        노원역 = saveStation(NOWON_STATION_NAME);
        대림역 = saveStation(DEARIM_STATION_NAME);
        파람역 = saveStation(PARAM_STATION_NAME);
    }

    @DisplayName("지하철 노선에 구간을 추가하면 노선 역이름 조회시 추가되있어야 한다.")
    @Test
    void 구간추가() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        // when
        lineService.addSection(line.getId(), new SectionAddRequest(선릉역.getId(), 수원역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(대림역.getId(), 강남역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(선릉역.getId(), 노원역.getId(), 2, 4));

        // then
        List<Station> stations = lineRepository.findById(line.getId())
                .orElse(null)
                .getStations();

        assertThat(stations).hasSize(5)
                .containsExactly(대림역, 강남역, 선릉역, 노원역, 수원역);
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재할 경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두존재() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        // when then
        assertThatThrownBy(() -> lineService.addSection(line.getId(), new SectionAddRequest(강남역.getId(), 선릉역.getId(), 3, 4)))
                .isExactlyInstanceOf(SectionExistException.class)
                .hasMessage("구간 상행역, 하행역이 이미 노선에 등록되어 있습니다.");
    }

    @DisplayName("지하철 노선 추가 시 노선에 구간에 역이 둘다 존재하지 않을경우 에러를 던진다.")
    @Test
    void 구간추가_노선에_추가역_모두미존재() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        // when then
        assertThatThrownBy(() -> lineService.addSection(line.getId(), new SectionAddRequest(노원역.getId(), 대림역.getId(), 3, 4)))
                .isExactlyInstanceOf(SectionNotExistException.class)
                .hasMessage("구간 상행역, 하행역이 노선에 하나도 포함되어있지 않습니다.");
    }

    @DisplayName("지하철 노선에 구간시 기존구간에 길이를 초과하면 에러를 던진다.")
    @Test
    void 구간추가_기존구간_길이초과() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        lineService.addSection(line.getId(), new SectionAddRequest(선릉역.getId(), 수원역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(대림역.getId(), 강남역.getId(), 3, 4));

        // when then
        assertThatThrownBy(() -> lineService.addSection(line.getId(), new SectionAddRequest(노원역.getId(), 선릉역.getId(), 14, 4)))
                .isExactlyInstanceOf(SectionDistanceOverException.class)
                .hasMessage("구간길이를 초과했습니다.");
    }

    @DisplayName("지하철 노선에 등록된 역을 조회하면 지금까지 등록된 모든 역에 정보가 조회되야 한다.")
    @Test
    void 노선_역_조회() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        lineService.addSection(line.getId(), new SectionAddRequest(선릉역.getId(), 수원역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(대림역.getId(), 강남역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(노원역.getId(), 선릉역.getId(), 1, 4));

        // when
        LineResponse lineResponse = lineService.findLine(line.getId());

        // then
        assertThat(lineResponse.getStations()).hasSize(5)
                .extracting("name")
                .containsExactly(
                        대림역.getName(),
                        강남역.getName(),
                        노원역.getName(),
                        선릉역.getName(),
                        수원역.getName()
                );
    }

    @DisplayName("지하철 노선에 구간을 삭제하면 노선 역이름 조회시 삭제한 역은 제외되야 한다.")
    @Test
    void 구간삭제() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        lineService.addSection(line.getId(), new SectionAddRequest(선릉역.getId(), 수원역.getId(), 3, 4));
        lineService.addSection(line.getId(), new SectionAddRequest(수원역.getId(), 노원역.getId(), 3, 4));

        // when
        lineService.deleteSection(line.getId(), 노원역.getId());

        // then
        assertThat(lineService.findLine(line.getId()).getStations()).hasSize(3)
                .extracting("name")
                .containsExactly(
                        강남역.getName(),
                        선릉역.getName(),
                        수원역.getName()
                );
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간정보가 1개이므로 삭제가 실패되어야 한다.")
    @Test
    void 구간삭제_구간최소갯수() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        // when then
        assertThatThrownBy(() -> lineService.deleteSection(line.getId(), 선릉역.getId()))
                .isExactlyInstanceOf(SectionDeleteMinSizeException.class)
                .hasMessage("구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("지하철 노선 추가 후 구간 삭제시 구간에 포함된 역이 아닌경우 삭제에 실패되어야 한다.")
    @Test
    void 구간삭제_구간미포함_역() {
        // given
        Line line = saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 10, 5);

        line.addSection(선릉역, 수원역, 3,4);

        // when then
        assertThatThrownBy(() -> lineService.deleteSection(line.getId(), 노원역.getId()))
                .isExactlyInstanceOf(SectionNotFoundException.class)
                .hasMessage("구간정보를 찾을 수 없습니다.");
    }

    @Nested
    class ShortPath {

        @BeforeEach
        void setUp() {
            saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, SHINBUNDANG_LINE_SURCHARGE, 강남역, 선릉역, 2, 3);
            saveLine(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 5)
                    .addSection(수원역, 대림역, 48, 10);
            saveLine(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 1)
                    .addSection(노원역, 수원역, 9, 4);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 이동거리가 가장 짧은 경로를 리턴해야한다.")
        @Test
        void 강남역_수원역_이동거리_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 수원역.getId(), 비회원);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
                "신분당선 추가요금이 붙은 할인되지 않은 금액이 리턴되야한다.")
        @Test
        void 강남역_수원역_비회원_요금_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 수원역.getId(), 비회원);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
            assertThat(shortPathResponse.getFare()).isEqualTo(2150);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
                "신분당선 추가요금이 붙은 할인되지 않은 금액이 리턴되야한다.")
        @Test
        void 강남역_수원역_성인_요금_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 수원역.getId(), 성인);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
            assertThat(shortPathResponse.getFare()).isEqualTo(2150);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
                "신분당선 추가요금이 붙은 어린이 할인이 적용된 금액이 리턴되야한다.")
        @Test
        void 강남역_수원역_어린이_요금_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 수원역.getId(), 어린이);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
            assertThat(shortPathResponse.getFare()).isEqualTo(900);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 최단경로는 신분당선과 이호선을 거쳐 이동하는 경로이며 " +
                "신분당선 추가요금이 붙은 청소년 할인이 적용된 금액이 리턴되야한다.")
        @Test
        void 강남역_수원역_청소년_요금_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 수원역.getId(), 청소년);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDistance()).isEqualTo(5);
            assertThat(shortPathResponse.getDuration()).isEqualTo(8);
            assertThat(shortPathResponse.getFare()).isEqualTo(1440);
        }

        @DisplayName("강남역에서 수원역으로 가는 경로조회시 소요시간이 가장 짧은 경로를 리턴해야한다.")
        @Test
        void 강남역_수원역_소요시간_검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DURATION, 강남역.getId(), 수원역.getId(), new AnonymousPrincipal());

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(3)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 노원역.getName(), 수원역.getName());
            assertThat(shortPathResponse.getDuration()).isEqualTo(5);
            assertThat(shortPathResponse.getDistance()).isEqualTo(14);
            assertThat(shortPathResponse.getFare()).isEqualTo(1350);
        }

        @DisplayName("이동거리가 50KM 이상인 경우에 요금금액 검증")
        @Test
        void 강남역_대림역_금액검증() {
            // when
            ShortPathResponse shortPathResponse = lineService.findShortPath(DISTANCE, 강남역.getId(), 대림역.getId(), 비회원);

            // then
            assertThat(shortPathResponse.getStations())
                    .hasSize(4)
                    .extracting("name")
                    .containsExactly(강남역.getName(), 선릉역.getName(), 수원역.getName(), 대림역.getName());
            assertThat(shortPathResponse.getDuration()).isEqualTo(18);
            assertThat(shortPathResponse.getDistance()).isEqualTo(53);
            assertThat(shortPathResponse.getFare()).isEqualTo(3050);
        }

        @DisplayName("최단경로 조회 역중 노선에 포함되지 않은 역이 존재할 경우 에러를 던진다.")
        @Test
        void 경로조회_미포함역() {
            // when then
            assertThatThrownBy(() -> lineService.findShortPath(DISTANCE, 선릉역.getId(), 파람역.getId(), new AnonymousPrincipal()))
                    .isExactlyInstanceOf(StationNotExistException.class)
                    .hasMessage("노선에 역이 존재하지 않습니다.");
        }

        @DisplayName("최단경로 조회 시작역, 종착역이 동일할 경우 에러를 던진다.")
        @Test
        void 경로조회_시작역_종착역_동일() {
            // when then
            assertThatThrownBy(() -> lineService.findShortPath(DISTANCE, 대림역.getId(), 대림역.getId(), new AnonymousPrincipal()))
                    .isExactlyInstanceOf(ShortPathSameStationException.class)
                    .hasMessage("최단경로 시작역, 종착역이 동일할 수 없습니다.");
        }

    }

    private Station saveStation(String stationName) {
        return stationRepository.save(new Station(stationName));
    }

    private Line saveLine(String name, String color, Integer surcharge, Station upStation, Station downStation, int distance, int duration) {
        return lineRepository.save(new Line(name, color, surcharge, upStation, downStation, distance, duration));
    }

    private Line saveLine(String name, String color, Station upStation, Station downStation, int distance, int duration) {
        return lineRepository.save(new Line(name, color, upStation, downStation, distance, duration));
    }

}
