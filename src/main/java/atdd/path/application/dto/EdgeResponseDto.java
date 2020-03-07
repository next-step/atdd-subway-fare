package atdd.path.application.dto;

import atdd.path.domain.Edge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EdgeResponseDto {
    private Long lineId;
    private Long sourceStationId;
    private Long targetStationId;
    private int distance;
    private int elapsedTime;

    @Builder
    public EdgeResponseDto(Long lineId, Long sourceStationId, Long targetStationId, int distance, int elapsedTime) {
        this.lineId = lineId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
        this.elapsedTime = elapsedTime;
    }

    public static EdgeResponseDto of(Edge savedEdge) {
        return EdgeResponseDto.builder()
                .lineId(savedEdge.getLine().getId())
                .sourceStationId(savedEdge.getSourceStation().getId())
                .targetStationId(savedEdge.getTargetStation().getId())
                .build();
    }
}
