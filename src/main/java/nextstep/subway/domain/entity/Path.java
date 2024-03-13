package nextstep.subway.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.response.PathResponse;
import nextstep.subway.domain.response.StationResponse;
import nextstep.subway.strategy.PathStrategy;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Path {
    private PathStrategy pathStrategy;
    private List<Station> stations;
    private int shortestValue;

    public Path(PathStrategy pathStrategy) {
        this.pathStrategy = pathStrategy;
    }

    public PathResponse createPathResponse(List<Section> allSectionList) {
        List<StationResponse> stationList = new ArrayList<>();

        for (Station station : this.stations) {
            stationList.add(new StationResponse().from(station));
        }

        Sections sections = getSections(allSectionList);
        return new PathResponse(stationList, sections.getDistance(), sections.getDuration());
    }

    public Sections getSections(List<Section> allSections) {
        List<Section> sectionList = new ArrayList<>();

        List<Station> stations = this.getStations();
        for (int i = 0; i < stations.size() - 1; i++) {
            Station upStation = stations.get(i);
            Station downStation= stations.get(i+1);

            Section pathSection = allSections.stream()
                    .filter(section -> section.isSameAsUpStation(upStation))
                    .filter(section -> section.isSameAsDwonStation(downStation))
                    .findFirst().get();

            sectionList.add(pathSection);
        }
        return new Sections(sectionList);
    }
}
