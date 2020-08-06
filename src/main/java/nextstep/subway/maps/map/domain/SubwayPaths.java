package nextstep.subway.maps.map.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SubwayPaths {
    List<SubwayPath> subwayPaths = new ArrayList<>();

    public SubwayPaths(List<SubwayPath> subwayPaths) {
        this.subwayPaths.addAll(subwayPaths);
    }

    public SubwayPath getEarliestAlightTimePath(LocalTime time) {
        return subwayPaths.stream()
                .max((path1, path2) -> {
                    LocalTime path1AlightTime = path1.getAlightTime(time);
                    LocalTime path2AlightTime = path2.getAlightTime(time);

                    return path1AlightTime.compareTo(path2AlightTime);
                })
                .orElseThrow(RuntimeException::new);
    }
}
