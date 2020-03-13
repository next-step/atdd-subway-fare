package atdd.path.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Timetables {
    private List<LocalTime> up = new ArrayList<>();
    private List<LocalTime> down  = new ArrayList<>();

    @Builder
    public Timetables(List<LocalTime> up, List<LocalTime> down) {
        this.up = up;
        this.down = down;
    }
}
