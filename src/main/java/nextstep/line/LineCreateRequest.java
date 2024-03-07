package nextstep.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.alg.util.Pair;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class LineCreateRequest {
    private String name;
    private String color;
    private int extraFare;
    private int distance;
    private int duration;
    private String firstDepartureTime;
    private String lastDepartureTime;
    private int intervalTime;
    private Long upstationId;
    private Long downstationId;

    public Line toEntity() {
        Pair<LocalTime, LocalTime> departureTimes = parseDepartureTime();

        return Line.builder()
                .name(name)
                .color(color)
                .extraFare(extraFare)
                .firstDepartureTime(departureTimes.getFirst())
                .lastDepartureTime(departureTimes.getSecond())
                .intervalTime(intervalTime)
                .build();
    }

    private Pair<LocalTime, LocalTime> parseDepartureTime() {
        LocalTime firstTime = null;
        LocalTime lastTime = null;

        if (firstDepartureTime != null && !firstDepartureTime.isEmpty()) {
            firstTime = LocalTime.parse(firstDepartureTime, DateTimeFormatter.ISO_LOCAL_TIME);
        }
        if (lastDepartureTime != null && !lastDepartureTime.isEmpty()) {
            lastTime = LocalTime.parse(lastDepartureTime, DateTimeFormatter.ISO_LOCAL_TIME);
        }

        return Pair.of(firstTime, lastTime);
    }
}
