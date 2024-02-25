package nextstep.subway.line.section.domain;

import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    public static final int FIRST_INDEX = 0;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    @OrderBy("upStation.id ASC")
    private List<Section> sectionList = new LinkedList<>();

    protected Sections() {
    }

    private Sections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public static Sections from(List<Section> sectionList) {
        return new Sections(sectionList);
    }

    public List<Station> startStations() {
        return this.sectionList.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private Section firstSection() {
        return this.sectionList.get(0);
    }

    private Section lastSection() {
        return this.sectionList.get(lastIndex());
    }

    public Station lastStation() {
        return lastSection().getDownStation();
    }

    private int lastIndex() {
        return this.sectionList.size() - 1;
    }

    public ApplyValues add(Section section) {
        if (isAlreadyAdded(section)) {
            throw new IllegalArgumentException("이미 추가된 구간입니다.");
        }
        return addTarget(section);
    }

    private ApplyValues addTarget(Section section) {
        if (canAddFirst(section)) {
            this.sectionList.add(FIRST_INDEX, section);
            return ApplyValues.applyAddFirst(section.distance(), section.duration());
        }

        if (canAddLast(section)) {
            this.sectionList.add(section);
            return ApplyValues.applyAddLast(section.distance(), section.duration());
        }

        addMiddle(section);
        return ApplyValues.applyAddMiddle();
    }

    private boolean isAlreadyAdded(Section section) {
        return this.sectionList.stream()
                .anyMatch(s -> s.anyMatchUpStationAndDownStation(section));
    }

    private void addMiddle(Section section) {
        ApplyPosition applyPosition = ApplyPosition.of(this.sectionList, section, PositionType.ADD_MIDDLE);
        Section existing = this.sectionList.get(applyPosition.findingIndex());
        existing.changeSectionFromToInput(applyPosition, section);
        this.sectionList.add(applyPosition.applyIndex(), section);
    }

    private boolean canAddFirst(Section section) {
        return firstSection().isSameUpStationInputDownStation(section);
    }

    private boolean canAddLast(Section section) {
        return lastSection().isSameDownStationInputUpStation(section);
    }

    public ApplyValues delete(Station station) {
        if (this.sectionList.size() == 1) {
            throw new IllegalArgumentException("구간이 하나 일 때는 삭제를 할 수 없습니다.");
        }
        return deleteTarget(station);
    }

    public boolean existStation(Station station) {
        return this.sectionList.stream()
                .anyMatch(section -> section.anyMatchUpStationAndDownStation(station));
    }

    private ApplyValues deleteTarget(Station station) {
        Section targetSection;

        if (canDeleteFirst(station)) {
            targetSection = firstSection();
            this.sectionList.remove(targetSection);
            return ApplyValues.applyDeleteFirst(targetSection.distance(), targetSection.duration());
        }

        if (canDeleteLast(station)) {
            targetSection = lastSection();
            this.sectionList.remove(targetSection);
            return ApplyValues.applyDeleteLast(targetSection.distance(), targetSection.duration());
        }

        deleteMiddle(station);
        return ApplyValues.applyDeleteMiddle();
    }

    private Section deleteMiddle(Station station) {
        ApplyPosition applyPosition = ApplyPosition.of(this.sectionList, station, PositionType.DELETE_MEDDLE);
        Section section = this.sectionList.get(applyPosition.findingIndex());
        Section targetSection = this.sectionList.get(applyPosition.applyIndex());
        section.changeDownStationFromToInputDownStation(targetSection);
        this.sectionList.remove(targetSection);
        return targetSection;
    }

    private boolean canDeleteFirst(Station station) {
        return firstSection().isSameUpStation(station);
    }

    private boolean canDeleteLast(Station station) {
        return lastSection().isSameDownStation(station);
    }

    public Stations stations() {
        return Stations.from(this.sectionList.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toList()));
    }

    public List<Section> getAll() {
        return this.sectionList;
    }

    public void forEach(Consumer<Section> action) {
        sectionList.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections = (Sections) o;
        return Objects.equals(sectionList, sections.sectionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionList);
    }

    public Long calculateValue(Station source,
                               Station target,
                               PathType type) {
        return this.sectionList.stream()
                .filter(section -> section.isSameUpStation(source) && section.isSameDownStation(target))
                .map(type::findBy)
                .reduce(0L, Long::sum);
    }
}
