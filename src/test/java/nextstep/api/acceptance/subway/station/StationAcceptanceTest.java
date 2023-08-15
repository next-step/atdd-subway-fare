package nextstep.api.acceptance.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.api.acceptance.AcceptanceTest;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철역을 생성한다")
    @Test
    void createStation() {
        final String name = "강남역";

        // when
        StationSteps.지하철역_생성_성공(name);

        // then
        Assertions.assertThat(StationSteps.지하철역_단일조회_성공(name)).hasSize(1);
    }

    @DisplayName("모든 지하철역 목록을 조회한다")
    @Test
    void showStations() {
        final List<String> names = List.of("강남역", "역삼역");

        // given
        StationSteps.지하철역_생성_성공(names);

        // when
        final var responses = StationSteps.지하철역_전체조회_성공();

        // then
        assertThat(responses).hasSize(names.size());
    }

    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation() {
        final String name = "강남역";

        // given
        final Long stationId = StationSteps.지하철역_생성_성공(name).getId();

        // when
        StationSteps.지하철역_제거_성공(stationId);

        // then
        Assertions.assertThat(StationSteps.지하철역_전체조회_성공()).isEmpty();
    }
}
