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
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException();
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

            if (!section.isPresent()) {
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

    private void rearrangeSectionWithDownStation(Section section) {
        sections.stream()
                .filter(it -> it.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> {
                    // 신규 구간의 상행역과 기존 구간의 상행역에 대한 구간을 추가한다.
                    sections.add(
                            new Section
                                    .SectionBuilder()
                                    .line(section.getLine())
                                    .upStation(it.getUpStation())
                                    .downStation(section.getUpStation())
                                    .distance(it.getDistance() - section.getDistance())
                                    .duration(section.getDuration())
                                    .build());
                    sections.remove(it);
                });
    }

    private void rearrangeSectionWithUpStation(Section section) {
        sections.stream()
                .filter(it -> it.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> {
                    // 신규 구간의 하행역과 기존 구간의 하행역에 대한 구간을 추가한다.
                    sections.add(
                            new Section
                                    .SectionBuilder()
                                    .line(section.getLine())
                                    .upStation(section.getDownStation())
                                    .downStation(it.getDownStation())
                                    .distance(it.getDistance() - section.getDistance())
                                    .duration(section.getDuration())
                                    .build());
                    sections.remove(it);
                });
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
            Section newSection =
                    new Section
                            .SectionBuilder()
                            .line(upSection.get().getLine())
                            .upStation(upSection.get().getDownStation())
                            .downStation(downSection.get().getUpStation())
                            .distance(upSection.get().getDistance() + downSection.get().getDistance())
                            .duration(upSection.get().getDuration())
                            .build();

            this.sections.add(newSection);
        }
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

    public int totalDistance() {
        return sections.stream().mapToInt(Section::getDistance).sum();
    }

    public int totalDuration() {
        return sections.stream().mapToInt(Section::getDuration).sum();
    }
}
