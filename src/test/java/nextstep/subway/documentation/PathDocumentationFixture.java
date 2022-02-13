package nextstep.subway.documentation;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.Arrays;

public class PathDocumentationFixture {

    public static PathResponse PATH_RESPONSE_FIXTURE;

    static {
        PATH_RESPONSE_FIXTURE = new PathResponse(
                Arrays.asList(
                        createStationResponse(1L, "강남역"),
                        createStationResponse(2L, "역삼역"),
                        createStationResponse(3L, "선릉역")
                ), 10, 15
        );
    }

    private static StationResponse createStationResponse(long id, String name) {
        return new StationResponse(id, name, LocalDateTime.now(), LocalDateTime.now());
    }

}
