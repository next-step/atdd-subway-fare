package nextstep.line.domain;

import nextstep.line.domain.exception.IllegalSectionOperationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.line.domain.exception.IllegalSectionOperationException.ALREADY_INCLUDED;
import static nextstep.line.domain.exception.IllegalSectionOperationException.NOT_ENOUGH_SECTION;

@Embeddable
public class Sections {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        checkDuplicateSection(section);

        rearrangeSectionWithSameUpStation(section);
        rearrangeSectionWithSameDownStation(section);

        sections.add(section);
    }

    private void checkDuplicateSection(Section section) {
        sections.stream()
                .filter(it -> it.hasDuplicateSection(section.getUpStationId(), section.getDownStationId()))
                .findFirst()
                .ifPresent(it -> {
                    throw new IllegalSectionOperationException(ALREADY_INCLUDED);
                });
    }

    private void rearrangeSectionWithSameUpStation(Section addedSection) {
        sections.stream()
                .filter(it -> it.isSameUpStation(addedSection.getUpStationId()))
                .findFirst()
                .ifPresent(it -> {
                    sections.add(it.subtract(addedSection));
                    sections.remove(it);
                });
    }

    private void rearrangeSectionWithSameDownStation(Section addedSection) {
        sections.stream()
                .filter(it -> it.isSameDownStation(addedSection.getDownStationId()))
                .findFirst()
                .ifPresent(it -> {
                    sections.add(it.subtract(addedSection));
                    sections.remove(it);
                });
    }

    public void delete(Long stationId) {
        if (sections.size() <= 1) {
            throw new IllegalSectionOperationException(NOT_ENOUGH_SECTION);
        }

        Optional<Section> downSectionOpt = findSectionByDownStation(stationId);
        Optional<Section> upSectionOpt = findSectionByUpStation(stationId);

        if (downSectionOpt.isPresent() && upSectionOpt.isPresent()) {
            Section downSection = downSectionOpt.get();
            Section upSection = upSectionOpt.get();
            sections.add(downSection.combine(upSection));
        }

        downSectionOpt.ifPresent(it -> sections.remove(it));
        upSectionOpt.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findSectionByUpStation(Long stationId) {
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(stationId))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Long stationId) {
        return this.sections.stream()
                .filter(it -> it.isSameDownStation(stationId))
                .findFirst();
    }

    public List<Long> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> result = new ArrayList<>();

        Optional<Section> sectionOpt = findSectionByUpStation(findFirstStation());
        while (sectionOpt.isPresent()) {
            Section section = sectionOpt.get();
            result.add(section.getUpStationId());
            sectionOpt = findSectionByUpStation(section.getDownStationId());

            if (sectionOpt.isEmpty()) {
                result.add(section.getDownStationId());
                break;
            }
        }

        return result;
    }

    private Long findFirstStation() {
        List<Long> upStations = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());

        List<Long> downStations = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toList());

        return upStations.stream()
                .filter(it -> !downStations.contains(it))
                .findFirst()
                .orElseThrow();
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

    public List<Section> getSections() {
        return sections;
    }
}
