package nextstep.core.subway.pathFinder.fixture;

import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.domain.PathFinderType;

public class PathFinderFixture {

    public static String 경로_조회_최단거리_타입 = PathFinderType.DISTANCE.name();
    public static String 경로_조회_소요_시간_타입 = PathFinderType.DURATION.name();

    public static PathFinderRequest 지하철_경로(Long 출발역, Long 도착역, String 경로_조회_타입) {
        return new PathFinderRequest(출발역, 도착역, 경로_조회_타입);
    }
}
