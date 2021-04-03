package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    private final int BASIC_FARE = 1250;
    private final int OVER_10KM_MAX_FARE = 800;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section section) {
        boolean isUpStationExisted = getStations().stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = getStations().stream().anyMatch(it -> it == section.getDownStation());
        checkValidation(section, isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpSection(section);
        }

        if (isDownStationExisted) {
            updateDownSection(section);
        }

        sections.add(section);
    }

    private void updateDownSection(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance(), section.getDuration()));
    }

    private void updateUpSection(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance(), section.getDuration()));
    }

    private void checkValidation(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!sections.isEmpty() && getStations().stream().noneMatch(it -> it == section.getUpStation()) &&
                getStations().stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeSection(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            int newDuration = upLineStation.get().getDuration() + downLineStation.get().getDuration();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance, newDuration));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public int getTotalDistance() {
        return sections.stream().mapToInt(it -> it.getDistance()).sum();
    }

    public int getTotalDuration() {
        return sections.stream().mapToInt(it -> it.getDuration()).sum();
    }

    public int getTotalFare() {
        int distance = getTotalDistance();

        return BASIC_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        int over10KmFare = calculateOver10KmFare(distance);
        int over50KmFare = calculateOver50KmFare(distance);
        return over10KmFare + over50KmFare;
    }

    private int calculateOver10KmFare(int distance) {
        distance -= 10;
        if (distance > 0) {
            int calculatedFare = (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
            return Math.min(OVER_10KM_MAX_FARE, calculatedFare);
        }
        return 0;
    }

    private int calculateOver50KmFare(int distance) {
        distance -= 50;
        return distance > 0 ? (int) ((Math.ceil((distance - 1) / 8) + 1) * 100) : 0;
    }
}
