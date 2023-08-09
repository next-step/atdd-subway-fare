package nextstep.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Long duration;

}
