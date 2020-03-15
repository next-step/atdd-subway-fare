package atdd.path.application;

import atdd.TestConstant;
import atdd.path.application.dto.StationTimetableDto;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = StationService.class)
public class StationServiceTest {

    @Autowired
    private StationService stationService;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private LineRepository lineRepository;

    @DisplayName("지하철역의 시간표를 조회한다")
    @Test
    void retrieveStationTimetable() {
        // given
        given(stationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(TestConstant.TEST_STATION));
        given(lineRepository.findAllById(anyList()))
                .willReturn(Arrays.asList(TestConstant.TEST_LINE, TestConstant.TEST_LINE_2));

        // when
        List<StationTimetableDto> stationTimeTable = stationService.retrieveStationTimetable(TestConstant.STATION_ID);

        // then
        assertThat(stationTimeTable.size()).isEqualTo(2);
    }

}