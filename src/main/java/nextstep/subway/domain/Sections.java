package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
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

        Optional<Section> upSection = findSectionAs(it -> it.isSameUpStation(station));
        Optional<Section> downSection = findSectionAs(it -> it.isSameDownStation(station));

        if (upSection.isPresent() && downSection.isPresent()) {
            addNewSectionForDelete(upSection.get(), downSection.get());
        }

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

    public int totalDistance() {
        return getTotalAs(Section::getDistance);
    }

    public int totalDuration() {
        return getTotalAs(Section::getDuration);
    }

    private void checkDuplicateSection(Section section) {
        sections.forEach(it -> {
            if (it.hasDuplicateSection(section)) {
                throw new IllegalArgumentException();
            }
        });
    }

    private void rearrangeSectionWithDownStation(Section section) {
        findSectionAs(it -> it.isSameUpStation(section.getDownStation()))
                .ifPresent(it -> {
                    // 신규 구간의 상행역과 기존 구간의 상행역에 대한 구간을 추가한다.
                    sections.add(Section.createFrontSectionOf(it, section));
                    sections.remove(it);
                });
    }

    private void rearrangeSectionWithUpStation(Section section) {
        findSectionAs(it -> it.isSameUpStation(section.getUpStation()))
                .ifPresent(it -> {
                    // 신규 구간의 하행역과 기존 구간의 하행역에 대한 구간을 추가한다.
                    sections.add(Section.createBackSectionOf(it, section));
                    sections.remove(it);
                });
    }

    private Station findFirstUpStation() {
        List<Station> upStations = getStations(Section::getUpStation);
        List<Station> downStations = getStations(Section::getDownStation);

        downStations.forEach(upStations::remove);
        return upStations.get(0);
    }

    private void addNewSectionForDelete(Section upSection, Section downSection) {
        this.sections.add(Section.of(upSection, downSection));
    }

    private Optional<Section> findSectionAs(Predicate<Section> predicate) {
        return this.sections.stream()
                .filter(predicate)
                .findFirst();
    }

    private Optional<Section> findSectionAsUpStation(Station finalUpStation) {
        return this.sections.stream()
                .filter(it -> it.isSameUpStation(finalUpStation))
                .findFirst();
    }

    private List<Station> getStations(Function<Section, Station> mapper) {
        return this.sections.stream()
                .map(mapper)
                .collect(Collectors.toUnmodifiableList());
    }

    private int getTotalAs(ToIntFunction<Section> mapper) {
        return sections.stream()
                .mapToInt(mapper)
                .sum();
    }
}
