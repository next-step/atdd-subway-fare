package nextstep.subway.domain;

import nextstep.subway.ui.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MIN_COUNT_CONDITION_SECTION_REMOVE = 2;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    void addSection(Section newSection) {
        validateAddSectionStationNotExistInSection(newSection);
        for (Section existingSection : sections) {
            existingSection.updateAddLineBetweenSection(newSection);
        }
        sections.add(newSection);
    }

    void remove(Station downStation) {
        validateOneSection();
        Section removeSection = sections.stream()
                .filter(equalsDownStation(downStation))
                .findFirst()
                .orElseThrow(() ->
                        new SectionException(String.format("상행역과 하행역 모두 구간에 존재하지 않는 역입니다. 하행역 = %s", downStation)));
        sections.remove(removeSection);

        for (Section section : sections) {
            section.updateRemoveLineBetweenSection(removeSection);
        }
    }

    List<Station> getStations() {
        return getStations(getFirstSection());
    }

    boolean isEmpty() {
        return sections.isEmpty();
    }

    int size() {
        return sections.size();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    private Predicate<Section> equalsDownStation(Station downStation) {
        return section -> section.getDownStation().equals(downStation);
    }

    private void validateAddSectionStationNotExistInSection(Section section) {
        if (isNotExistsStationInSection(section)) {
            throw new SectionException(
                    String.format("상행역과 하행역 모두 구간에 존재하지 않는 역입니다. 상행역 = %s, 하행역 = %s",
                            section.getUpStation().getName(), section.getDownStation().getName()));
        }
    }

    private boolean isNotExistsStationInSection(Section section) {
        return !getUpStations().contains(section.getUpStation()) &&
                !getUpStations().contains(section.getDownStation()) &&
                !getDownStations().contains(section.getUpStation()) &&
                !getDownStations().contains(section.getDownStation()) &&
                !sections.isEmpty();
    }

    private void validateOneSection() {
        if (sections.size() < MIN_COUNT_CONDITION_SECTION_REMOVE) {
            throw new SectionException("구간이 1개 이하인 경우 삭제할 수 없습니다.");
        }
    }

    private List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private List<Station> getStations(Section firstSection) {
        List<Station> stations = new ArrayList<>();
        addEndUpSectionStation(firstSection, stations);
        addEndDownStation(stations);
        return stations;
    }

    private Section getFirstSection() {
        return sections.stream()
                .filter(section ->
                        getDownStations().stream()
                        .noneMatch(equalsUpAndDownStation(section)))
                .findFirst()
                .orElse(sections.get(0));
    }

    private Predicate<Station> equalsUpAndDownStation(Section section) {
        return downStation -> downStation.equals(section.getUpStation());
    }

    /**
     *
     * @param firstSection : getFirstSection()에서 가져온 상행 종점 구간이다.
     * @param stations : 빈 객체로 최초로 상행 종점 구간의 역을 추가할 객체이다.
     */
    private void addEndUpSectionStation(Section firstSection, List<Station> stations) {
        stations.add(firstSection.getUpStation());
        stations.add(firstSection.getDownStation());
    }

    /**
     * 상행 종점 구간의 하행역 부터 시작하여 추가되는 하행 종점역과 각 구간의 상행역이 같으면 각 구간의 하행역을 추가한다.
     * @param stations : getFirstSection()에서 가져온 상행 종점 구간의 상행 종점역과 하행역
     */
    private void addEndDownStation(List<Station> stations) {
        for (int i = 0; i < stations.size(); i++) {
            addDownStations(stations);
        }
    }

    private void addDownStations(List<Station> stations) {
        for (Section section : sections) {
            addDownStation(stations, section);
        }
    }

    private void addDownStation(List<Station> stations, Section section) {
        Station endDownStation = stations.get(stations.size() - 1);
        if (endDownStation.equals(section.getUpStation())) {
            stations.add(section.getDownStation());
        }
    }

    /**
     * 최단 거리 검색 시
     * -> 구간 시간의 합을 반환
     * 최소 시간 검색 시
     * -> 구간 거리의 합을 반환
     *
     * 검색 기준이 아닌 시간 or 거리는 단순히 구간의 합을 반환한다.
     *
     * 메서드 명을 어떻게 해야할까? 음.. 생각이 나지 않는다.
     */
    int ddd(List<Station> stations, PathType type) {
        int sum = 0;
        for (Section section : sections) {
            for (int i = 0; i < stations.size() - 1; i++) {
                if (section.getUpStation().equals(stations.get(i))) {
                    if (section.getDownStation().equals(stations.get(i + 1))) {
                        if (type == PathType.DISTANCE) {
                            sum += section.getDuration();
                        }
                        if (type == PathType.DURATION) {
                            sum += section.getDistance();
                        }
                        break;
                    }
                }
            }
        }
        return sum;
    }
}
