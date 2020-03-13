package atdd.path.application.dto;

import atdd.path.domain.Timetables;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
