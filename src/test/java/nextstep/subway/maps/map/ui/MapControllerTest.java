package nextstep.subway.maps.map.ui;

import nextstep.subway.maps.map.application.MapService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.members.member.domain.LoginMember;
import nextstep.subway.members.member.domain.Member;
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
        MapService mapService = mock(MapService.class);
        MapController controller = new MapController(mapService);
        when(mapService.findPath(any(), anyLong(), anyLong(), any())).thenReturn(new PathResponse());

        ResponseEntity<PathResponse> entity = controller.findPath(null, 1L, 2L, PathType.DISTANCE,null);

        verify(mapService).findPath(nullable(LoginMember.class), anyLong(), anyLong(), any(PathType.class));
        assertThat(entity.getBody()).isNotNull();
    }

    @Test
    void findPathWithUser() {
        //given
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 15);
        MapService mapService = mock(MapService.class);
        MapController controller = new MapController(mapService);
        when(mapService.findPath(any(), anyLong(), anyLong(), any())).thenReturn(new PathResponse());

        ResponseEntity<PathResponse> entity = controller.findPath(loginMember, 1L, 2L, PathType.DISTANCE, null);

        verify(mapService).findPath(any(LoginMember.class), anyLong(), anyLong(), any(PathType.class));
        assertThat(entity.getBody()).isNotNull();
    }

    @Test
    void findMap() {
        MapService mapService = mock(MapService.class);
        MapController controller = new MapController(mapService);

        ResponseEntity entity = controller.findMap();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(mapService).findMap();
    }
}
