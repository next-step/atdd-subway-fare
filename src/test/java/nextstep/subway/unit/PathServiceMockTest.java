package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.constant.FindPathType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("최단 경로 조회 테스트")
public class PathServiceMockTest {

    @InjectMocks
    private PathService pathService;
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;

    @DisplayName("경로 조회에 따른 기본 요금을 응답한다.")
    @Test
    void findPath() {
        // given : 선행조건 기술
        Station 교대역 = createStation(1L, "교대역");
        Station 양재역 = createStation(2L, "양재역");
        Line line = createLine(1L, "2호선");
        line.addSection(교대역, 양재역, 10, 10);
        given(lineService.findLines()).willReturn(List.of(line));
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(2L)).willReturn(양재역);


        // when : 기능 수행
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(response.getFare()).isEqualTo(1250);
    }

    @DisplayName("경로 조회에 따른 추가 요금을 응답한다. - 거리 기준 16km")
    @Test
    void findPath2() {
        // given : 선행조건 기술
        Station 교대역 = createStation(1L, "교대역");
        Station 양재역 = createStation(2L, "양재역");
        Line line = createLine(1L, "2호선");
        line.addSection(교대역, 양재역, 16, 10);
        given(lineService.findLines()).willReturn(List.of(line));
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(2L)).willReturn(양재역);


        // when : 기능 수행
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(response.getFare()).isEqualTo(1450);
    }

    @DisplayName("경로 조회에 따른 추가 요금을 응답한다. - 거리 기준 59km")
    @Test
    void findPath3() {
        // given : 선행조건 기술
        Station 교대역 = createStation(1L, "교대역");
        Station 양재역 = createStation(2L, "양재역");
        Line line = createLine(1L, "2호선");
        line.addSection(교대역, 양재역, 59, 10);
        given(lineService.findLines()).willReturn(List.of(line));
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(2L)).willReturn(양재역);


        // when : 기능 수행
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(response.getFare()).isEqualTo(2250);
    }

    private Station createStation(Long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }

    private Line createLine(Long id, String name) {
        Line line = new Line(name, "bg-red-600");
        ReflectionTestUtils.setField(line, "id", id);
        return line;
    }

}
