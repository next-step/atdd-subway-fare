package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.domain.GraphModel;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.entity.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;
import static nextstep.common.constant.PathType.DISTANCE;
import static nextstep.common.constant.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GraphModelTest {

    Station 강남역;
    Station 역삼역;
    Station 논현역;
    Section 강남역_역삼역_구간;

    Sections 구간들;
    Line 신분당선;
    GraphModel 그래프_기본_모델;
    List<Line> 지하철_리스트;

    Long 기본_노선_추가요금 = 0L;
    Long 신분당선_기본_거리 = 15L;
    Long 총_거리 = 5L;
    Long 총_시간 = 10L;
    Long 총_비용 = null;

    Member 로그인_사용자_비할인대상;


    @BeforeEach
    public void setup() {
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");
        논현역 = Station.of(3L, "논현역");

        강남역_역삼역_구간 = Section.of(강남역, 역삼역, 총_거리, 총_시간);

        구간들 = new Sections(List.of(강남역_역삼역_구간));
        신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 구간들, 기본_노선_추가요금);

        그래프_기본_모델 = GraphModel.of(1L, 2L);

        지하철_리스트 = Collections.singletonList(신분당선);
        로그인_사용자_비할인대상 = Member.of(1L, "test@test.com", "password", 20);

    }

    @DisplayName("[createGraphModel] graph를 distance 기준으로 생성한다.")
    @Test
    void createGraphModel_distance_success() {
        // when
        그래프_기본_모델.createGraphModel(Collections.singletonList(신분당선), DISTANCE.getValue());

        // then
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = 그래프_기본_모델.getGraph();
        DefaultWeightedEdge edge = graph.getEdge(강남역.getId(), 역삼역.getId());

        assertAll(
                () -> assertTrue(graph.containsVertex(강남역.getId())),
                () -> assertTrue(graph.containsVertex(역삼역.getId())),
                () -> assertTrue(graph.containsEdge(강남역.getId(), 역삼역.getId())),
                () -> assertNotNull(edge),
                () -> assertEquals((long) graph.getEdgeWeight(edge), 총_거리)
        );
    }

    @DisplayName("[createGraphModel] graph를 duration 기준으로 생성한다.")
    @Test
    void createGraphModel_duration_success() {
        // when
        그래프_기본_모델.createGraphModel(Collections.singletonList(신분당선), DURATION.getValue());

        // then
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = 그래프_기본_모델.getGraph();
        DefaultWeightedEdge edge = graph.getEdge(강남역.getId(), 역삼역.getId());

        assertAll(
                () -> assertTrue(graph.containsVertex(강남역.getId())),
                () -> assertTrue(graph.containsVertex(역삼역.getId())),
                () -> assertTrue(graph.containsEdge(강남역.getId(), 역삼역.getId())),
                () -> assertNotNull(edge),
                () -> assertEquals((long) graph.getEdgeWeight(edge), 총_시간)
        );
    }

    @DisplayName("[createGraphModel] 출발역과 도착역이 같은 section을 가진 linelist는 예외를 발생시킨다.")
    @Test
    void createGraphModel_fail() {
        // given
        var 강남역_강남역_구간 = Section.of(강남역, 강남역, 5L, 5L);
        var 구간들 = new Sections(List.of(강남역_강남역_구간));
        var 신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 구간들, 기본_노선_추가요금);

        // when & then
        Assertions.assertThrows(PathException.class, () -> 그래프_기본_모델.createGraphModel(Collections.singletonList(신분당선), DISTANCE.getValue()))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[createGraphModel] Linelist가 비어있으면 예외가 발생한다.")
    @Test
    void createGraphModel_fail2() {
        // when & then
        Assertions.assertThrows(PathException.class, () -> 그래프_기본_모델.createGraphModel(List.of(), DISTANCE.getValue()))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[createGraphModel] Linelist의 Sections가 비어있으면 예외가 발생한다.")
    @Test
    void createGraphModel_fail3() {
        // given
        var 구간들 = new Sections(List.of());
        var 신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 구간들, 기본_노선_추가요금);

        // when & then
        Assertions.assertThrows(PathException.class, () -> 그래프_기본_모델.createGraphModel(Collections.singletonList(신분당선), DISTANCE.getValue()))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[findPath] 총 거리 기준으로 Path를 생성한다.")
    @Test
    void findPath_distance_success() {
        // given
        그래프_기본_모델.createGraphModel(지하철_리스트, DISTANCE.getValue());

        // when
        Path path = 그래프_기본_모델.findPath(로그인_사용자_비할인대상, 지하철_리스트, DISTANCE.getValue());

        // then
        assertAll(
                () -> assertNotNull(path),
                () -> assertThat(총_거리).isEqualTo(path.getTotalDistance()),
                () -> assertThat(총_시간).isEqualTo(path.getTotalDuration()),
                () -> assertThat(총_비용).isEqualTo(path.getTotalPrice()),
                () -> assertThat(List.of(강남역, 역삼역)).isEqualTo(path.getStations())
        );
    }

    @DisplayName("[findPath] 총 시간 기준으로 Path를 생성한다.")
    @Test
    void findPath_duration_success() {
        // given
        var 강남역_논현역_구간_거리 = 15L;
        var 강남역_논현역_구간_시간 = 2L;
        var 논현역_역삼역_구간_거리 = 5L;
        var 논현역_역삼역_구간_시간 = 1L;
        Long 강남역_논현역_역삼역_총_비용 = null;
        var 강남역_논현역_구간 = Section.of(강남역, 논현역, 강남역_논현역_구간_거리, 강남역_논현역_구간_시간);
        var 논현역_역삼역_구간 = Section.of(논현역, 역삼역, 논현역_역삼역_구간_거리, 논현역_역삼역_구간_시간);
        var 구간들 = new Sections(List.of(강남역_역삼역_구간, 강남역_논현역_구간, 논현역_역삼역_구간));
        var 신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 구간들, 기본_노선_추가요금);
        지하철_리스트 = Collections.singletonList(신분당선);
        그래프_기본_모델.createGraphModel(지하철_리스트, DURATION.getValue());

        // when
        Path path = 그래프_기본_모델.findPath(로그인_사용자_비할인대상, 지하철_리스트, DURATION.getValue());

        // then
        assertAll(
                () -> assertNotNull(path),
                () -> assertThat(강남역_논현역_구간_거리 + 논현역_역삼역_구간_거리).isEqualTo(path.getTotalDistance()),
                () -> assertThat(강남역_논현역_구간_시간 + 논현역_역삼역_구간_시간).isEqualTo(path.getTotalDuration()),
                () -> assertThat(강남역_논현역_역삼역_총_비용).isEqualTo(path.getTotalPrice()),
                () -> assertThat(List.of(강남역, 논현역, 역삼역)).isEqualTo(path.getStations())
        );
    }

    @DisplayName("[findPath] lineList가 비어있으면 예외가 발생한다.")
    @Test
    void findPath_fail1() {
        // given
        그래프_기본_모델.createGraphModel(지하철_리스트, DISTANCE.getValue());

        // when & then
        assertAll(
                () -> assertThrows(PathException.class, () -> 그래프_기본_모델.findPath(로그인_사용자_비할인대상, List.of(), DISTANCE.getValue()))
                        .getMessage().equals(PATH_NOT_FOUND.getDescription())
        );
    }

    @DisplayName("[getStationList] lineList와 stationId를 통해 StationList를 생성한다.")
    @Test
    void getStationList_success() {
        // when
        var 생성된_StationList = 그래프_기본_모델.getStations(지하철_리스트, List.of(강남역.getId(), 역삼역.getId()));

        // then
        assertAll(
                () -> assertThat(생성된_StationList).containsExactlyInAnyOrder(강남역, 역삼역)
        );
    }

    @DisplayName("[getStationList] lineList에 StationId가 없다면 예외가 발생한다.")
    @Test
    void getStationList_fail2() {
        // when & then
        assertThrows(PathException.class, () -> 그래프_기본_모델.getStations(지하철_리스트, List.of(강남역.getId(), 논현역.getId())))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[addSectionsToGraph] line을 graph의 Edge에 distance 기준으로 추가한다.")
    @Test
    public void addSectionsToGraph_distance_success() {
        // when
        그래프_기본_모델.addSectionsToGraph(신분당선, DISTANCE.getValue());

        // then
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = 그래프_기본_모델.getGraph();
        DefaultWeightedEdge edge = graph.getEdge(강남역.getId(), 역삼역.getId());

        assertAll(
                () -> assertTrue(graph.containsVertex(강남역.getId())),
                () -> assertTrue(graph.containsVertex(역삼역.getId())),
                () -> assertNotNull(edge),
                () -> assertEquals(graph.getEdgeWeight(edge), 5.0),
                () -> assertTrue(graph.containsEdge(강남역.getId(), 역삼역.getId()))
        );
    }

    @DisplayName("[addSectionsToGraph] line을 graph의 Edge에 duration 기준으로 추가한다.")
    @Test
    public void addSectionsToGraph_duration_success() {
        // when
        그래프_기본_모델.addSectionsToGraph(신분당선, DURATION.getValue());

        // then
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = 그래프_기본_모델.getGraph();
        DefaultWeightedEdge edge = graph.getEdge(강남역.getId(), 역삼역.getId());

        assertAll(
                () -> assertTrue(graph.containsVertex(강남역.getId())),
                () -> assertTrue(graph.containsVertex(역삼역.getId())),
                () -> assertNotNull(edge),
                () -> assertEquals((long) graph.getEdgeWeight(edge), 총_시간),
                () -> assertTrue(graph.containsEdge(강남역.getId(), 역삼역.getId()))
        );
    }

    @DisplayName("[addSectionsToGraph] Sections가 비어 있는 Line을 graph의 Edge에 추가하면 예외가 발생한다.")
    @Test
    public void addSectionsToGraph_fail() {
        // given
        var 빈_구간들 = new Sections(List.of());
        var 빈_구간을_가진_신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 빈_구간들, 기본_노선_추가요금);
        GraphModel graphModel = GraphModel.of(1L, 2L);

        // when & then
        Assertions.assertThrows(PathException.class, () -> graphModel.addSectionsToGraph(빈_구간을_가진_신분당선, "distance"))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[addSectionsToGraph] 동일한 StationId를 가지고 있는 Section을 graph의 Edge에 추가하면 예외가 발생한다.")
    @Test
    public void addSectionsToGraph_fail2() {
        // given
        var 강남역_강남역_구간 = Section.of(강남역, 강남역, 5L, 5L);
        var 구간들 = new Sections(List.of(강남역_강남역_구간));
        var 신분당선 = Line.of(1L, "신분당선", "red", 신분당선_기본_거리, 구간들, 기본_노선_추가요금);
        var graphModel = GraphModel.of(1L, 2L);

        // when & then
        Assertions.assertThrows(PathException.class, () -> graphModel.addSectionsToGraph(신분당선, "distance"))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[addEdge] 새로운 Edge를 생성한다.")
    @Test
    public void addEdge_success() {
        // given
        Section section = Section.of(1L, 강남역, 역삼역, 총_거리, 총_시간);
        그래프_기본_모델.addEdge(강남역.getId(), 역삼역.getId(), section, DISTANCE.getValue());

        // when
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = 그래프_기본_모델.getGraph();
        DefaultWeightedEdge edge = graph.getEdge(강남역.getId(), 역삼역.getId());

        // then
        assertAll(
                () -> assertTrue(graph.containsVertex(강남역.getId())),
                () -> assertTrue(graph.containsVertex(역삼역.getId())),
                () -> assertTrue(edge != null),
                () -> assertTrue((long) graph.getEdgeWeight(edge) == 총_거리)
        );
    }

    @DisplayName("[addEdge] 동일한 source와 target으로는 Edge를 생성할 수 없다.")
    @Test
    public void addEdge_fail() {
        // given
        Section section = Section.of(1L, 강남역, 역삼역, 20L, 20L);

        // then
        Assertions.assertThrows(PathException.class, () -> 그래프_기본_모델.addEdge(4L, 4L, section, DISTANCE.getValue()))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[validateDuplicate] 동일하지 않는 source와 target을 인자로 주면 예외가 발생하지 않는다.")
    @Test
    public void validateDuplicate_success() {
        // given
        var source = 1L;
        var target = 2L;
        var graphModel = GraphModel.of(1L, 2L);

        // when & then
        Assertions.assertDoesNotThrow(() -> graphModel.validateDuplicate(source, target));
    }

    @DisplayName("[validateDuplicate] 동일한 source와 target을 인자로 주면 예외가 발생한다.")
    @Test
    public void validateDuplicate_fail() {
        // given
        var source = 1L;
        var target = 1L;
        var graphModel = GraphModel.of(1L, 2L);

        // when & then
        Assertions.assertThrows(PathException.class, () -> graphModel.validateDuplicate(source, target))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[getStation] stationId에 해당하는 Station을 찾는다. upStation으로 찾는다.")
    @Test
    void getStation_success() {
        // given
        var 지하철_목록 = List.of(신분당선);

        // when
        var 찾은_역 = 그래프_기본_모델.getStation(지하철_목록, 강남역.getId());

        // then
        assertAll(
                () -> assertEquals(찾은_역, 강남역)
        );
    }

    @DisplayName("[getStation] stationId에 해당하는 Station을 찾는다. downStation으로 찾는다.")
    @Test
    void getStation_success2() {
        // given
        var 지하철_목록 = List.of(신분당선);

        // when
        var 찾은_역 = 그래프_기본_모델.getStation(지하철_목록, 역삼역.getId());

        // then
        assertAll(
                () -> assertEquals(찾은_역, 역삼역)
        );
    }

    @DisplayName("[getStation] stationId에 해당하는 Station을 찾지 못하면 예외가 발생한다.")
    @Test
    void getStation_fail1() {
        // given
        var 지하철_목록 = List.of(신분당선);

        // when & then
        assertThrows(PathException.class, () -> 그래프_기본_모델.getStation(지하철_목록, 3L))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[getStation] lineList가 비어 있으면 예외가 발생한다.")
    @Test
    void getStation_fail2() {
        // when & then
        assertThrows(PathException.class, () -> 그래프_기본_모델.getStation(List.of(), 3L))
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }
}

