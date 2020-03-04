package atdd.path.application.domain;

import java.time.LocalTime;
import java.util.List;

public class TimeTables {
    private List<LocalTime> up;
    private List<LocalTime> down;

    public TimeTables() {
    }

    public TimeTables(List<LocalTime> up) {
        this.up = up;
    }

    public TimeTables(List<LocalTime> up, List<LocalTime> down) {
        this.up = up;
        this.down = down;
    }
}
