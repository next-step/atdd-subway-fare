package nextstep.subway.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        createStation("강남역");

        // then
        var stationNames = getStations().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역의 목록 조회")
    @Test
    void getStationsTest() {
        //given
        createStations(List.of("수유역", "강변역"));

        //when
        var resultStationNames = getStations().getList("name", String.class);

        //then
        Assertions.assertEquals(2, resultStationNames.size());
        Assertions.assertEquals("수유역", resultStationNames.get(0));
        Assertions.assertEquals("강변역", resultStationNames.get(1));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStationTest() {
        //given
        var stationId = createStation("홍대입구역");

        //when
        deleteStation(stationId);

        //then
        var getStationsResponse = getStations().getList("name", String.class);

        Assertions.assertEquals(0, getStationsResponse.size());
    }
}
