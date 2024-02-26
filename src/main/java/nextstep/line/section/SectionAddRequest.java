package nextstep.line.section;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionAddRequest {
    private Long upstationId;
    private Long downstationId;
    private int distance;
    private int duration;
}
