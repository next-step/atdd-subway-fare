package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PathInfo {
    private List<String> stationIds;
    private int distance;
    private int duration;
}
