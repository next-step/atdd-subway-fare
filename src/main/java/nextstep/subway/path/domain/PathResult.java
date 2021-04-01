package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.dto.LineRequest.DEFAULT_EXTRA_CHARGE;

public class PathResult {

    private final Sections sections;
    private final Stations stations;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public Integer getLineMaxExtraCharge() {
        List<Integer> extraCharges = getExtraCharges();

        if (extraCharges.isEmpty()) {
            return DEFAULT_EXTRA_CHARGE;
        }
        return Collections.max(extraCharges);
    }

    private List<Integer> getExtraCharges() {
        return getSectionsContainsLines().stream()
                .map(Line::getExtraCharge)
                .filter(extraCharge -> extraCharge > DEFAULT_EXTRA_CHARGE)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    private List<Line> getSectionsContainsLines() {
        return sections.getSectionContainsLines();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }
}
