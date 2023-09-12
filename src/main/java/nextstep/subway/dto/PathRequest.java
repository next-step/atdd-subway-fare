package nextstep.subway.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.subway.domain.PathType;

@NoArgsConstructor
@Getter @Setter
public class PathRequest {

    @NotNull(message = "출발역은 반드시 입력해야합니다.")
    private Long source;

    @NotNull(message = "도착역은 반드시 입력해야합니다.")
    private Long target;

    @NotNull(message = "경로 타입은 반드시 입력해야합니다.")
    private PathType type;
}
