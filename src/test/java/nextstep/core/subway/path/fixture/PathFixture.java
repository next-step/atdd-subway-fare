package nextstep.core.subway.path.fixture;

import nextstep.core.subway.path.application.dto.PathRequest;
import nextstep.core.subway.path.domain.PathType;

public class PathFixture {

    public static String 경로_조회_최단거리_타입 = PathType.DISTANCE.name();
    public static String 경로_조회_최소_시간_타입 = PathType.DURATION.name();

    public static PathRequest 지하철_경로(Long 출발역, Long 도착역, String 경로_조회_타입) {
        return new PathRequest(출발역, 도착역, 경로_조회_타입);
    }

    public static String 경로_조회_타입_찾기(String 경로_조회_타입) {
        if(경로_조회_타입.equals("최단거리")) {
            return PathType.DISTANCE.name();
        }
        if(경로_조회_타입.equals("최소 소요시간")) {
            return PathType.DURATION.name();
        }
        return null;
    }
}
