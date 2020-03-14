package atdd.path.application.dto;

import atdd.path.domain.Line;
import atdd.path.domain.Timetables;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StationTimeTableDto {
    private Long lineId;
    private String lineName;
    private Timetables timetables;

    @Builder
    public StationTimeTableDto(Long lineId, String lineName, Timetables timetables) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.timetables = timetables;
    }

    public static StationTimeTableDto of(Line line, Long stationId) {
        return StationTimeTableDto.builder()
                .lineId(line.getId())
                .lineName(line.getName())
                .timetables(Timetables.of(line, stationId))
                .build();
    }

    public static List<StationTimeTableDto> listOf(List<Line> lines, Long stationId) {
        return lines.stream()
                .map(it -> StationTimeTableDto.of(it, stationId))
                .collect(Collectors.toList());
    }
}
