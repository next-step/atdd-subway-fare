package nextstep.subway.domain;

import nextstep.common.exception.NoDeleteOneSectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.common.error.SubwayError.NO_DELETE_ONE_SECTION;

@Embeddable
public class Sections {
    public static final int ONE_SECTION = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    public Sections(final List<Section> sections) {
        this.sections = sections;
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

    public void delete(final Station station) {
        if (this.sections.size() <= ONE_SECTION) {
            throw new NoDeleteOneSectionException(NO_DELETE_ONE_SECTION);
        }
        Optional<Section> upSection = findSectionAsUpStation(station);
        Optional<Section> downSection = findSectionAsDownStation(station);

        addNewSectionForDelete(upSection, downSection);

        upSection.ifPresent(it -> this.sections.remove(it));
        downSection.ifPresent(it -> this.sections.remove(it));
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

    private void checkDuplicateSection(final Section section) {
        sections.stream()
                .filter(it -> it.hasDuplicateSection(section.getUpStation(), section.getDownStation()))
                .findFirst()
                .ifPresent(it -> {
                    throw new IllegalArgumentException();
                });
    }

    private void rearrangeSectionWithDownStation(final Section section) {
        sections.stream()
                .filter(it -> it.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(currentStation -> addUpStation(section, currentStation));
    }

    private void addUpStation(final Section section, final Section currentStation) {
        sections.add(new Section(section.getLine(), currentStation.getUpStation(), section.getUpStation(),
                currentStation.getDistance() - section.getDistance(),
                currentStation.getDuration() - section.getDuration()));
        sections.remove(currentStation);
    }

    private void rearrangeSectionWithUpStation(final Section section) {
        sections.stream()
                .filter(it -> it.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(currentStation -> addDownStation(section, currentStation));
    }

    private void addDownStation(final Section section, final Section currentStation) {
        sections.add(new Section(section.getLine(), section.getDownStation(), currentStation.getDownStation(),
                currentStation.getDistance() - section.getDistance(),
                currentStation.getDuration() - section.getDuration()));
        sections.remove(currentStation);
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

    private void addNewSectionForDelete(Optional<Section> upSection, Optional<Section> downSection) {
        if (upSection.isPresent() && downSection.isPresent()) {
            final Section newSection = Section.of(upSection.get(), downSection.get());
            this.sections.add(newSection);
        }
    }

    private Optional<Section> findSectionAsUpStation(Station finalUpStation) {
        return this.sections.stream()
                .filter(section -> section.isSameUpStation(finalUpStation))
                .findFirst();
    }

    private Optional<Section> findSectionAsDownStation(Station station) {
        return this.sections.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst();
    }

    public int totalDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public int totalDuration() {
        return sections.stream()
                .mapToInt(Section::getDuration)
                .sum();
    }

    public Fare getMaxExtraFare() {
        return sections.stream()
                .map(Section::getLine)
                .map(line -> line.getExtraFare().getFare())
                .max(BigDecimal::compareTo)
                .map(Fare::new)
                .orElse(Fare.from(0));
    }

    public List<Section> getSections() {
        return sections;
    }
}
