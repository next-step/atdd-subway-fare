package nextstep.subway.maps.map.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.maps.map.application.FareMapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.FarePathResponse;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.members.member.domain.LoginMember;

public class MapControllerTest {

    @Test
    void findMap() {
        FareMapService mapService = mock(FareMapService.class);
        MapController controller = new MapController(mapService);

        ResponseEntity<MapResponse> entity = controller.findMap();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(mapService).findSubwayMap();
    }

    @Test
    void findPathWithUser() {
        // given
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 15);
        FareMapService mapService = mock(FareMapService.class);
        MapController controller = new MapController(mapService);
        when(mapService.findPathWithFare(any(), anyLong(), anyLong(), any())).thenReturn(new FarePathResponse());

        // when
        ResponseEntity<FarePathResponse> entity = controller.findPath(loginMember, 1L, 2L, PathType.DISTANCE, null);

        // then
        verify(mapService).findPathWithFare(any(LoginMember.class), anyLong(), anyLong(), any(PathType.class));
        assertThat(entity.getBody()).isNotNull();
    }
}
