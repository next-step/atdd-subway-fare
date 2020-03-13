package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StationSimpleResponseView {
    private long id;
    private String name;

    public StationSimpleResponseView() {
    }

    public StationSimpleResponseView(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationSimpleResponseView> listOf(List<Station> stations) {
        List<StationSimpleResponseView> stationSimpleResponseViews = new ArrayList<>();

        stations.forEach(data -> stationSimpleResponseViews.add(new StationSimpleResponseView(data.getId(), data.getName())));

        return stationSimpleResponseViews;
    }
}
