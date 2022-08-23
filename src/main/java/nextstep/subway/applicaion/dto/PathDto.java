package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.PathType;

@Getter
@AllArgsConstructor
public class PathDto {

    private Long source;
    private Long target;
    private PathType type;

    public static PathDto of(Long source, Long target, PathType type) {
        return new PathDto(source, target, type);
    }
}
