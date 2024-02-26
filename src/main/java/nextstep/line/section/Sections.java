package nextstep.line.section;


import lombok.extern.slf4j.Slf4j;
import nextstep.exception.InvalidInputException;
import nextstep.station.Station;
import org.jgrapht.alg.util.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
@Slf4j
public class Sections implements Iterable<Section> {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    @OrderColumn(name = "sections_sequence")
    private List<Section> sections = new ArrayList<>();

    @Override
    public Iterator<Section> iterator() {
        return sections.iterator();
    }

    public int size() {
        return sections.size();
    }

    public Stream<Section> stream() {
        return sections.stream();
    }

    public int getTotalDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public int getTotalDuration() {
        return sections.stream()
                .mapToInt(Section::getDuration)
                .sum();
    }

    public void initSection(Section section) {
        sections.add(section);
    }

    public boolean isFirstUpstation(Station station) {
        return getFirstUpstation().equals(station);
    }

    public boolean isLastDownstation(Station station) {
        return getLastDownstation().equals(station);
    }

    public Station getFirstUpstation() {
        return sections.get(0).getUpstation();
    }

    public Station getLastDownstation() {
        return sections.get(sections.size() - 1).getDownstation();
    }

    public Section getNextSection(Section currentSection) {
        boolean next = false;
        for (Section section : sections) {
            if (next) {
                return section;
            }
            if (section.equals(currentSection)) {
                next = true;
            }
        }
        return null;
    }

    public void addFirstSection(Section newSection) {
        if (sections.stream().anyMatch(section ->
                section.isInSection(newSection))) {
            throw new InvalidInputException("새로운 구간의 하행역은 이미 노선에 존재하는 역이면 안 됩니다.");
        }

        sections.add(0, newSection);
    }

    private boolean isUpstationExistsInLine(Section newSection) {
        return sections.stream().anyMatch(section ->
                section.isUpstation(newSection.getUpstation()) ||
                        section.isDownstation(newSection.getUpstation()));
    }

    private boolean isDownstationExistsInLine(Section newSection) {
        return sections.stream().anyMatch(section ->
                section.isUpstation(newSection.getDownstation()) ||
                        section.isDownstation(newSection.getDownstation()));
    }

    private Section addAndConnectSection(boolean upstationExists, boolean downstationExists, Section newSection) {
        if (upstationExists) {
            sections.stream()
                    .filter(section -> section.isUpstation(newSection.getUpstation()))
                    .findFirst()
                    .map(section -> {
                        section.setUpstation(newSection.getDownstation());
                        sections.add(sections.indexOf(section), newSection);

                        return section;
                    });
        } else {
            sections.stream()
                    .filter(section -> section.isDownstation(newSection.getDownstation()))
                    .findFirst()
                    .map(section -> {
                        section.setDownstation(newSection.getUpstation());
                        sections.add(sections.indexOf(section) + 1, newSection);

                        return section;
                    });
        }
        return null;
    }

    private Pair<Section, Section> removeAndDisconnectSection(Station stationToDelete) {
        sections.stream()
                .filter(section -> section.isDownstation(stationToDelete))
                .findFirst()
                .map(section -> {
                    Section nextSection = sections.get(sections.indexOf(section) + 1);
                    section.setDownstation(nextSection.getDownstation());
                    sections.remove(nextSection);
                    nextSection.setLine(null);

                    return Pair.of(section, nextSection);
                });
        return null;
    }

    private void updateDistanceAndDurationWhenAdd(Section newSection, Section nextSection) {
        int newDistance = nextSection.getDistance() - newSection.getDistance();
        if (newDistance <= 0) {
            throw new InvalidInputException("유효하지 않은 거리입니다.");
        }

        int newDuration = nextSection.getDuration() - newSection.getDuration();
        if (newDuration <= 0) {
            throw new InvalidInputException("유효하지 않은 소요시간입니다.");
        }

        nextSection.setDistance(newDistance);
        nextSection.setDuration(newDuration);
    }

    private void updateDistanceAndDurationWhenRemove(Section reconnectedSection, Section removedSection) {
        reconnectedSection.setDistance(reconnectedSection.getDistance() + removedSection.getDistance());
        reconnectedSection.setDuration(reconnectedSection.getDuration() + removedSection.getDuration());
    }

    public void addSection(Section newSection) {
        // newSection의 upstation, downstation 둘 다 노선에 등록되어있는 거면 안 됨
        boolean upstationExists = isUpstationExistsInLine(newSection);
        boolean downstationExists = isDownstationExistsInLine(newSection);

        if (upstationExists && downstationExists) {
            throw new InvalidInputException("새로운 구간의 상행역과 하행역 둘 다 이미 노선에 등록되어 있습니다.");
        }

        if (!upstationExists && !downstationExists) {
            throw new InvalidInputException("새로운 구간은 기존 노선의 역과 최소 하나 이상 연결되어야 합니다.");
        }

        Section nextSection = addAndConnectSection(upstationExists, downstationExists, newSection);
        updateDistanceAndDurationWhenAdd(newSection, nextSection);
    }

    public void addLastSection(Section newSection) {
        if (sections.stream().anyMatch(section ->
                section.isInSection(newSection))) {
            throw new InvalidInputException("새로운 구간의 하행역은 이미 노선에 존재하는 역이면 안 됩니다.");
        }

        sections.add(newSection);
    }

    public Section removeFirstSection() {
        Section removedSection = sections.remove(0);
        removedSection.setLine(null);

        return removedSection;
    }

    public Section removeLastSection() {
        Section removedSection = sections.remove(sections.size() - 1);
        removedSection.setLine(null);

        return removedSection;
    }

    public Section removeSection(Station stationToDelete) {

        Pair<Section, Section> pair = removeAndDisconnectSection(stationToDelete);

        Section reconnectedSection = pair.getFirst();
        Section removedSection = pair.getSecond();

        updateDistanceAndDurationWhenRemove(reconnectedSection, removedSection);

        return null;
    }

}
