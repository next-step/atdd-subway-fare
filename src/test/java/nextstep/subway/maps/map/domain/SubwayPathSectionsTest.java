package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathSectionsTest {

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        SubwayPathTestFixture fixture = new SubwayPathTestFixture();
        subwayPath = new SubwayPath(Lists.newArrayList(
                new LineStationEdge(fixture.lineStation1, fixture.line1),
                new LineStationEdge(fixture.lineStation2, fixture.line1),
                new LineStationEdge(fixture.lineStation4, fixture.line2),
                new LineStationEdge(fixture.lineStation6, fixture.line3)
        ));
    }

    @DisplayName("$DISPLAY_NAME$")
    @Test
    void extractPathSections() {
        // when
        SubwayPathSections subwayPathSections = SubwayPathSections.from(subwayPath.getLineStationEdges());

        // then
        assertThat(subwayPathSections.countLines()).isEqualTo(3);
    }

    @DisplayName("$DISPLAY_NAME$")
    @Test
    void getSourceStationId() {
        // given
        SubwayPathSections subwayPathSections = SubwayPathSections.from(subwayPath.getLineStationEdges());

        // when
        Long sourceStationId = subwayPathSections.getSourceStationId();

        // then
        assertThat(sourceStationId).isEqualTo(1L);
    }

    @DisplayName("$DISPLAY_NAME$")
    @Test
    void getTargetStationId() {
        // given
        SubwayPathSections subwayPathSections = SubwayPathSections.from(subwayPath.getLineStationEdges());

        // when
        Long sourceStationId = subwayPathSections.getTargetStationId();

        // then
        // 한바퀴 돌아서 옴
        assertThat(sourceStationId).isEqualTo(4L);
    }
}