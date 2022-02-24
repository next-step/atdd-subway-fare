package nextstep.subway.documentation;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;

import java.time.LocalDateTime;

public class PathDocumentationFixture {
    public static PathResponse PATH_RESPONSE;

    static {
        PATH_RESPONSE = new PathResponse(
                Lists.newArrayList(
                        createStationResponse(1L, "강남역"),
                        createStationResponse(2L, "역삼역"),
                        createStationResponse(3L, "선릉역")
                ), 60, 26, 1520, 1250, 300, 700, 730);
    }

    private static StationResponse createStationResponse(long id, String name) {
        return new StationResponse(id, name, LocalDateTime.now(), LocalDateTime.now());
    }
}
