package atdd.path.application.dto;

import atdd.path.domain.Line;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StationTimetablesResponseView {
    private long lineId;
    private String lineName;
    private TimetablesResponseView timetables;

    public StationTimetablesResponseView() {
    }

    @Builder
    private StationTimetablesResponseView(long lineId, String lineName, TimetablesResponseView timetables) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.timetables = timetables;
    }

    public static List<StationTimetablesResponseView> listOf(long stationId, List<Line> lines) {
        List<StationTimetablesResponseView> timetablesResponseViews = new ArrayList<>();

        for (Line line : lines) {
            StationTimetablesResponseView stationTimetablesResponseView = StationTimetablesResponseView.builder()
                    .lineId(line.getId())
                    .lineName(line.getName())
                    .timetables(TimetablesResponseView.of(line.getUpTimetablesOf(stationId), line.getDownTimetablesOf(stationId))).build();

            timetablesResponseViews.add(stationTimetablesResponseView);

        }

        return timetablesResponseViews;
    }
}
