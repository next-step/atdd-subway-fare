package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import support.auth.userdetails.User;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private String accessToken;

    @BeforeEach
    void setUp() {
        회원_생성_요청("test@test.com", "123", 0);
        accessToken = 로그인_되어_있음("test@test.com", "123");
    }

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")), 3, 5, 10);

        when(pathService.findPath("test@test.com", 1L, 2L, PathType.DISTANCE))
                .thenReturn(pathResponse);

        final ExtractableResponse<Response> response = 경로조회(1L, 2L, PathType.DISTANCE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(3);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
    }

    private ExtractableResponse<Response> 경로조회(Long source, Long target, PathType type) {
        return pathGiven()
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }
}
