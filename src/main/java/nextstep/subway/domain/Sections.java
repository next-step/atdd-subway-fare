package nextstep.subway.domain;

import java.util.function.Function;
import java.util.function.Predicate;
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
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException();
        }

        Optional<Section> upStation = findSectionBy(section -> section.isSameUpStation(station));
        Optional<Section> downStation = findSectionBy(section -> section.isSameDownStation(station));

        if (upStation.isPresent() && downStation.isPresent()) {
            addNewSectionForDelete(upStation.get(), downStation.get());
        }

        upStation.ifPresent(section -> this.sections.remove(section));
        downStation.ifPresent(section -> this.sections.remove(section));
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return getSortedStations();
    }

    private List<Station> getSortedStations() {
        Station upStation = findFirstUpStation();
        List<Station> result = new ArrayList<>();
        result.add(upStation);

        while (true) {
            Station finalUpStation = upStation;
            Optional<Section> section = findSectionBy(s -> s.isSameUpStation(finalUpStation));

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

    private Optional<Section> findSectionBy(Predicate<Section> isSameStation) {
        return this.sections.stream()
                .filter(isSameStation)
                .findFirst();
    }

    private void rearrangeSectionWithDownStation(final Section section) {
        sections.stream()
                .filter(it -> it.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(existingSection -> connectExistingUpStationAndNewUpStation(existingSection, section));
    }

    private void connectExistingUpStationAndNewUpStation(
            final Section existingSection,
            final Section newSection
    ) {
        Section section = existingSection.replaceDownStationWithUpStation(newSection);
        sections.add(section);
        sections.remove(existingSection);
    }

    private void rearrangeSectionWithUpStation(Section section) {
        sections.stream()
                .filter(it -> it.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(existingSection -> connectExistingDownStationAndNewDownStation(existingSection, section));
    }

    private void connectExistingDownStationAndNewDownStation(final Section existingSection, final Section newSection) {
        Section section = existingSection.replaceUpStationWithDownStation(newSection);
        sections.add(section);
        sections.remove(existingSection);
    }

    private Station findFirstUpStation() {
        List<Station> upStations = getStations(Section::getUpStation);
        List<Station> downStations = getStations(Section::getDownStation);

        return upStations.stream()
                .filter(it -> !downStations.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<Station> getStations(final Function<Section, Station> getUpStation) {
        return this.sections.stream()
                .map(getUpStation)
                .collect(Collectors.toList());
    }

    private void addNewSectionForDelete(Section upSection, Section downSection) {
        if (upSection != null && downSection != null) {
            Section newSection = new Section(
                    upSection.getLine(),
                    downSection.getUpStation(),
                    upSection.getDownStation(),
                    upSection.getDistance() + downSection.getDistance(),
                    upSection.getDuration() + downSection.getDuration()
            );

            this.sections.add(newSection);
        }
    }

    public int totalDistance() {
        return sections.stream().mapToInt(Section::getDistance).sum();
    }

    public int totalDuration() {
        return sections.stream().mapToInt(Section::getDuration).sum();
    }
}
