package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Timetables {
    private List<LocalTime> up = new ArrayList<>();
    private List<LocalTime> down = new ArrayList<>();

    @Builder
    public Timetables(List<LocalTime> up, List<LocalTime> down) {
        this.up = up;
        this.down = down;
    }

    public static Timetables of(Line line, Station station) {
        return Timetables.builder()
                .up(station.getTimetable(line, true))
                .down(station.getTimetable(line, false))
                .build();
    }

}
