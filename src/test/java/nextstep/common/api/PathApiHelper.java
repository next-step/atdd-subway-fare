package nextstep.common.api;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.RestAssuredHelper;
import nextstep.path.domain.PathType;

import java.util.Map;

public class PathApiHelper {

    public static final String PATH_API_PATH = "/paths";

    private PathApiHelper() {
    }

    public static ExtractableResponse<Response> findPath(final Long startStationId, final Long endStationId, final PathType pathType, final String accessToken) {
        return RestAssuredHelper.getWithAuth(PATH_API_PATH, accessToken, Map.of(
                "source", startStationId,
                "target", endStationId,
                "pathType", pathType
        ));
    }
}
