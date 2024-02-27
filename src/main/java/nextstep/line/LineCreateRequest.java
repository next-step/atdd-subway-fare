package nextstep.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineCreateRequest {
    private String name;
    private String color;
    private int extraFare;
    private int distance;
    private int duration;
    private Long upstationId;
    private Long downstationId;

    public static Line toEntity(LineCreateRequest request) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .extraFare(request.getExtraFare())
                .build();
    }
}
