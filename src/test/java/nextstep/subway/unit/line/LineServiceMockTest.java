package nextstep.subway.unit.line;

import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.LineRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.StationRepository;
import nextstep.subway.domain.entity.addition.SectionAdditionHandlerMapping;
import nextstep.subway.domain.entity.deletion.SectionDeletionHandlerMapping;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.service.LineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("구간 서비스 단위 테스트 with mock")
@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionAdditionHandlerMapping sectionAdditionHandlerMapping;

    @DisplayName("구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 역삼역.getId(), 10);
        SectionDeletionHandlerMapping handlerMapping = new SectionDeletionHandlerMapping();
        Line line = mock(Line.class);
        LineService lineService = new LineService(lineRepository, stationRepository, sectionAdditionHandlerMapping, handlerMapping);
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역.getId())).thenReturn(Optional.of(역삼역));
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        // when
        lineService.addSection(1L, sectionRequest);

        // then
        verify(line).addSection(any(), any());
    }

    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        Station 선릉역 = new Station(3L, "선릉역");
        Line 이호선 = mock(Line.class);

        SectionDeletionHandlerMapping handlerMapping = new SectionDeletionHandlerMapping();
        LineService lineService = new LineService(lineRepository, stationRepository, sectionAdditionHandlerMapping, handlerMapping);
        when(이호선.getId()).thenReturn(1L);
        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));
        when(stationRepository.findById(선릉역.getId())).thenReturn(Optional.of(선릉역));

        // when
        lineService.deleteSection(이호선.getId(), 선릉역.getId());

        // then
        verify(이호선).removeSection(handlerMapping, 선릉역);
    }
}
