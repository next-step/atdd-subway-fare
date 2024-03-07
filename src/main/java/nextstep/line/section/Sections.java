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

    public Section get(int i) {
        return sections.get(i);
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

    private void connectSection(boolean upstationExists, Section newSection, Section connectableSection) {
        if (upstationExists) {
            connectableSection.setUpstation(newSection.getDownstation());
            sections.add(sections.indexOf(connectableSection), newSection);
        } else {
            connectableSection.setDownstation(newSection.getUpstation());
            sections.add(sections.indexOf(connectableSection) + 1, newSection);
        }
    }

    private Section findConnectableSection(boolean upstationExists, Section newSection) {
        if (upstationExists) {
            return sections.stream()
                    .filter(section -> section.isUpstation(newSection.getUpstation()))
                    .findFirst()
                    .orElseThrow();
        }

        return sections.stream()
                .filter(section -> section.isDownstation(newSection.getDownstation()))
                .findFirst()
                .orElseThrow();
    }

    public void addSection(Section newSection) {
        boolean upstationExists = validateWhenAdd(newSection);

        Section connectableSection = findConnectableSection(upstationExists, newSection);
        connectSection(upstationExists, newSection, connectableSection);
        connectableSection.shorten(newSection);
    }

    private boolean validateWhenAdd(Section newSection) {
        boolean upstationExists = isUpstationExistsInLine(newSection);
        boolean downstationExists = isDownstationExistsInLine(newSection);

        if (upstationExists && downstationExists) {
            throw new InvalidInputException("새로운 구간의 상행역과 하행역 둘 다 이미 노선에 등록되어 있습니다.");
        }

        if (!upstationExists && !downstationExists) {
            throw new InvalidInputException("새로운 구간은 기존 노선의 역과 최소 하나 이상 연결되어야 합니다.");
        }

        return upstationExists;
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

    public void removeSection(Station stationToDelete) {

        Section disconnectableSection = findDisconnectableSection(stationToDelete);
        Section removedSection = disconnectSection(disconnectableSection);

        disconnectableSection.extend(removedSection);
    }

    private Section findDisconnectableSection(Station stationToDelete) {
        return sections.stream()
                .filter(section -> section.isDownstation(stationToDelete))
                .findFirst().orElseThrow();
    }

    private Section disconnectSection(Section disconnectableSection) {
        Section removedSection = sections.get(sections.indexOf(disconnectableSection) + 1);
        disconnectableSection.setDownstation(removedSection.getDownstation());
        sections.remove(removedSection);
        removedSection.setLine(null);

        return removedSection;
    }

}
