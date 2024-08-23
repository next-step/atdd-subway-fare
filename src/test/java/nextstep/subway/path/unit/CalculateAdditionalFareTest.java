package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.path.dto.CalculateAdditionalFare;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CalculateAdditionalFareTest {

    private Station 강남역;
    private Station 역삼역;
    private Section 강남역_역삼역_구간;
    private Sections 신분당선_구간 = new Sections();
    private Line 신분당선;
    private Long 기본_추가_요금 = 0L;
    private Long 추가_요금 = 500L;
    private Long 기존_요금 = 350L;
    private Long 거리_10이하 = 10L;
    private Long 시간_기본 = 5L;
    private Long 노선_거리_기본 = 10L;

    @BeforeEach
    void setup() {
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");
        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 거리_10이하, 시간_기본);
        신분당선_구간.addSection(강남역_역삼역_구간);
    }

    @Test
    @DisplayName("추가 요금이 없는 경우 기본 요금만 반환")
    void calculateLineAdditionalFare_NoAdditionalFare_ReturnsBasePrice() {
        // given
        Long 추가_요금_없음 = 0L;
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 추가_요금_없음);

        // when
        var totalFare = CalculateAdditionalFare.calculateLineAdditionalFare(List.of(신분당선), 신분당선_구간, 0L);

        // then
        assertAll(
                () -> assertThat(totalFare).isEqualTo(기본_추가_요금)
        );
    }

    @Test
    @DisplayName("추가 요금이 있는 경우 최댓값 반환")
    void calculateLineAdditionalFare_WithAdditionalFare_ReturnsMaxAdditionalFareAdded() {
        // given
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 추가_요금);

        // when
        var totalFare = CalculateAdditionalFare.calculateLineAdditionalFare(List.of(신분당선), 신분당선_구간, 기존_요금);

        // then
        assertAll(
                () -> assertThat(totalFare).isEqualTo(기존_요금 + 추가_요금)
        );
    }

    @Test
    @DisplayName("여러 구간에서 추가 요금이 있는 경우 최대 추가 요금 반환")
    void calculateLineAdditionalFare_MultipleSections_ReturnsMaxAdditionalFare() {
        // given
        var 추가_요금_최대 = 500L;
        var 추가_요금_최소 = 200L;
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 추가_요금_최대);
        var _2호선 = Line.of(2L, "2호선", "Blue", 노선_거리_기본, 신분당선_구간, 추가_요금_최소);

        // when
        var totalFare = CalculateAdditionalFare.calculateLineAdditionalFare(List.of(신분당선, _2호선), 신분당선_구간, 기존_요금);

        // then
        assertAll(
                () -> assertThat(totalFare).isEqualTo(기존_요금 + 추가_요금_최대)
        );
    }
}

