package nextstep.subway.unit.line.handler;

import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.addition.SectionAdditionHandlerMapping;
import nextstep.subway.domain.entity.deletion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionDeletionHandlerMappingTest {

    final int SECTION_DEFAULT_DISTANCE = 10;

    Station 강남역;
    Station 역삼역;
    Station 선릉역;
    Station 익명역;
    Line 이호선;

    SectionDeletionHandlerMapping handlerMapping = new SectionDeletionHandlerMapping();


    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        선릉역 = new Station(3L, "선릉역");
        익명역 = new Station(4L, "익명역");
        이호선 = new Line("이호선", "Green", SECTION_DEFAULT_DISTANCE, 강남역, 역삼역);
        이호선.addSection(new SectionAdditionHandlerMapping(), new Section(이호선, 역삼역, 선릉역, SECTION_DEFAULT_DISTANCE));

    }

    @DisplayName("상행 종착역이 속한 구간 제거하는 핸들러 반환")
    @Test
    void returnDeleteSectionAtTopHandler() {
        // when
        SectionDeletionHandler handler = handlerMapping.getHandler(이호선.getSections(), 강남역);

        // then
        assertThat(handler).isInstanceOf(DeleteSectionAtTopHandler.class);
        assertThat(handler.checkApplicable(이호선.getSections(), 강남역)).isTrue();
    }

    @DisplayName("하행 종착역이 속한 구간 제거하는 핸들러 반환")
    @Test
    void returnDeleteSectionAtLastHandler() {
        // when
        SectionDeletionHandler handler = handlerMapping.getHandler(이호선.getSections(), 선릉역);

        // then
        assertThat(handler).isInstanceOf(DeleteSectionAtLastHandler.class);
        assertThat(handler.checkApplicable(이호선.getSections(), 선릉역)).isTrue();
    }

    @DisplayName("상행 종착역과 하행종착역이 아닌 중간 구간 제거하는 핸들러 반환")
    @Test
    void returnDeleteSectionAtMiddleHandler() {
        // when
        SectionDeletionHandler handler = handlerMapping.getHandler(이호선.getSections(), 역삼역);

        // then
        assertThat(handler).isInstanceOf(DeleteSectionAtMiddleHandler.class);
        assertThat(handler.checkApplicable(이호선.getSections(), 역삼역)).isTrue();
    }
}
