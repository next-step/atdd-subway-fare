package nextstep.core.subway.pathFinder.fixture;

import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.domain.PathFinderType;

public class PathFinderFixture {

    public static String 경로_조회_최단거리_타입 = PathFinderType.DISTANCE.name();
    public static String 경로_조회_최소_시간_타입 = PathFinderType.DURATION.name();

    public static PathFinderRequest 지하철_경로(Long 출발역, Long 도착역, String 경로_조회_타입) {
        return new PathFinderRequest(출발역, 도착역, 경로_조회_타입);
    }

    public static String 경로_조회_타입_찾기(String 경로_조회_타입) {
        if(경로_조회_타입.equals("최단거리")) {
            return PathFinderType.DISTANCE.name();
        }
        if(경로_조회_타입.equals("최소 소요시간")) {
            return PathFinderType.DURATION.name();
        }
        return null;
    }
}
