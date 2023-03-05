package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        if (this.sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        checkDuplicateSection(section);

        rearrangeSectionWithUpStation(section);
        rearrangeSectionWithDownStation(section);

        sections.add(section);
    }

    public void delete(Station station) {
        validateOnlyOneSection();

        List<Section> findSections = findSectionByStation(station);

        mergeSection(findSections);
        removeSections(findSections);
    }

    private void removeSections(List<Section> findSections) {
        findSections.forEach(this::removeSection);
    }

    private void removeSection(Section removeSection) {
        this.sections.removeIf(section -> section.equals(removeSection));
    }

    private void mergeSection(List<Section> findSections) {
        if (findSections.size() == 2) {
            addMergeSection(findSections.get(0), findSections.get(1));
        }
    }

    private void addMergeSection(Section firstSection, Section secondSection) {
        Station newUpStation = firstSection.getUpStation();
        Station newDownStation = secondSection.getDownStation();
        int newDistance = firstSection.getDistance() + secondSection.getDistance();
        int newDuration = firstSection.getDuration() + secondSection.getDuration();
        this.sections.add(new Section(firstSection.getLine(), newUpStation, newDownStation, newDistance, newDuration));
    }

    private void validateOnlyOneSection() {
        if (this.sections.size() <= 1) {
            throw new IllegalStateException("등록된 구간이 2개보다 적을 경우 구간을 삭제할 수 없습니다.");
        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        Station upStation = findFirstUpStation();
        List<Station> result = new ArrayList<>();
        result.add(upStation);

        while (true) {
            Station finalUpStation = upStation;
            Optional<Section> section = findSectionAsUpStation(finalUpStation);

            if (section.isEmpty()) {
                break;
            }

            upStation = section.get().getDownStation();
            result.add(upStation);
        }

        return result;
    }

    private void checkDuplicateSection(Section section) {
        sections.stream()
                .filter(it -> it.hasDuplicateSection(section.getUpStation(), section.getDownStation()))
                .findFirst()
                .ifPresent(it -> {
                    throw new IllegalArgumentException();
                });
    }

    private void rearrangeSectionWithDownStation(Section newSection) {
        sections.stream()
                .filter(oldSection -> oldSection.isSameDownStation(newSection.getDownStation()))
                .findFirst()
                .ifPresent(oldSection -> {
                    addUpSection(newSection, oldSection);
                    sections.remove(oldSection);
                });
    }

    private void addUpSection(Section newSection, Section oldSection) {
        // 신규 구간의 상행역과 기존 구간의 상행역에 대한 구간을 추가한다.
        sections.add(new Section(
                newSection.getLine(),
                oldSection.getUpStation(),
                newSection.getUpStation(),
                oldSection.getDistance() - newSection.getDistance(),
                oldSection.getDuration() - newSection.getDuration())
        );
    }

    private void rearrangeSectionWithUpStation(Section newSection) {
        sections.stream()
                .filter(oldSection -> oldSection.isSameUpStation(newSection.getUpStation()))
                .findFirst()
                .ifPresent(oldSection -> {
                    addDownSection(newSection, oldSection);
                    sections.remove(oldSection);
                });
    }

    private void addDownSection(Section newSection, Section oldSection) {
        // 신규 구간의 하행역과 기존 구간의 하행역에 대한 구간을 추가한다.
        sections.add(new Section(
                newSection.getLine(),
                newSection.getDownStation(),
                oldSection.getDownStation(),
                oldSection.getDistance() - newSection.getDistance(),
                oldSection.getDuration() - newSection.getDuration())
        );
    }

    private Station findFirstUpStation() {
        List<Station> upStations = this.sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return upStations.stream()
                .filter(it -> !downStations.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Optional<Section> findSectionAsUpStation(Station finalUpStation) {
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(finalUpStation))
                .findFirst();
    }

    private Optional<Section> findSectionAsDownStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();
    }

    private List<Section> findSectionByStation(Station station) {
        List<Section> findSections = new ArrayList<>();
        findSectionAsDownStation(station).ifPresent(findSections::add);
        findSectionAsUpStation(station).ifPresent(findSections::add);
        return findSections;
    }

    public int totalDistance() {
        return sections.stream().mapToInt(Section::getDistance).sum();
    }

    public int totalDuration() {
        return sections.stream().mapToInt(Section::getDuration).sum();
    }
}
