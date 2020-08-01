package nextstep.subway.maps.map.ui;

import nextstep.subway.maps.map.application.FareMapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.FarePathResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MapControllerTest {
    @Test
    void findPath() {
        FareMapService mapService = mock(FareMapService.class);
        MapController controller = new MapController(mapService);
        when(mapService.findPathWithFare(anyLong(), anyLong(), any())).thenReturn(new FarePathResponse());

        ResponseEntity<FarePathResponse> entity = controller.findPath(1L, 2L, PathType.DISTANCE);

        assertThat(entity.getBody()).isNotNull();
    }

    @Test
    void findMap() {
        FareMapService mapService = mock(FareMapService.class);
        MapController controller = new MapController(mapService);

        ResponseEntity entity = controller.findMap();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(mapService).findSubwayMap();
    }
}
