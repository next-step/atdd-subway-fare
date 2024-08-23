package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.CalculateOverFare;
import nextstep.path.dto.Path;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CalculateOverFareTest {

    private Station 강남역;
    private Station 역삼역;
    private Section 강남역_역삼역_구간;
    private Sections 신분당선_구간 = new Sections();
    private Line 신분당선;
    private Long 기본_노선_추가요금 = 0L;

    Long 거리_10이하 = 10L;
    Long 거리_11이상_50이하 = 20L;
    Long 거리_50_초과 = 60L;
    Long 시간_기본 = 5L;
    Long 노선_거리_기본 = 10L;

    Member 사용자;

    @BeforeEach
    void setup() {
        사용자 = Member.of(1L, "test@test", "password", 20);
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");
    }

    @Test
    @DisplayName("거리 10 이하일 때 기본 요금 반환")
    void calculateOverFare_WithinMinDistance_ReturnsDefaultFarePrice() {
        // when
        Long fare = CalculateOverFare.calculateOverFare(거리_10이하);

        // then
        assertAll(
                () -> assertThat(fare).isEqualTo(1250L)
        );
    }

    @Test
    @DisplayName("거리 11~50 사이일 때 추가 요금 반환")
    void calculateOverFare_BetweenMinAndSecondDistance_ReturnsFareWithOverFareFirst() {
        // when
        Long fare = CalculateOverFare.calculateOverFare(거리_11이상_50이하);

        // then
        assertAll(
                () -> assertThat(fare).isEqualTo(1450L)
        );
    }

    @Test
    @DisplayName("거리 50 초과일 때 추가 요금 반환")
    void calculateOverFare_ExceedingSecondDistance_ReturnsFareWithOverFareSecond() {
        // when
        Long fare = CalculateOverFare.calculateOverFare(거리_50_초과);

        // then
        assertAll(
                () -> assertThat(fare).isEqualTo(1950L)
        );
    }

    @Test
    @DisplayName("경로가 10이 넘지않을 때, 요금 계산 및 설정")
    void of_ReturnsPathWithCalculatedFare_0() {
        // given
        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 거리_10이하, 시간_기본);
        신분당선_구간.addSection(강남역_역삼역_구간);
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 기본_노선_추가요금);

        var path = Path.of(사용자, List.of(신분당선), List.of(강남역, 역삼역), 신분당선_구간);

        // when
        var result = CalculateOverFare.of(path);

        // then
        assertAll(
                () -> assertThat(result.getTotalPrice()).isEqualTo(1250)
        );
    }

    @Test
    @DisplayName("경로가 20일 때, 요금 계산 및 설정")
    void of_ReturnsPathWithCalculatedFare_20() {
        // given
        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 거리_11이상_50이하, 시간_기본);
        신분당선_구간.addSection(강남역_역삼역_구간);
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 기본_노선_추가요금);

        var path = Path.of(사용자, List.of(신분당선), List.of(강남역, 역삼역), 신분당선_구간);

        // when
        var result = CalculateOverFare.of(path);

        // then
        assertAll(
                () -> assertThat(result.getTotalPrice()).isEqualTo(1450)
        );
    }

    @Test
    @DisplayName("경로가 51일 때, 요금 계산 및 설정")
    void of_ReturnsPathWithCalculatedFare_50() {
        // given
        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 거리_50_초과, 시간_기본);
        신분당선_구간.addSection(강남역_역삼역_구간);
        신분당선 = Line.of(1L, "신분당선", "Red", 노선_거리_기본, 신분당선_구간, 기본_노선_추가요금);

        var path = Path.of(사용자, List.of(신분당선), List.of(강남역, 역삼역), 신분당선_구간);

        // when
        var result = CalculateOverFare.of(path);

        // then
        assertAll(
                () -> assertThat(result.getTotalPrice()).isEqualTo(1950)
        );
    }
}

