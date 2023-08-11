package nextstep.subway.section.domain;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.exception.*;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    public static final int BASIC_FEE = 1250;
    private Long firstStationId;

    private Long lastStationId;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        firstStationId = sections.get(0).getUpStationId();
        lastStationId = sections.get(sections.size() - 1).getDownStationId();

        this.sections.addAll(sections);
    }

    public Sections(Long firstStationId, Long lastStationId) {
        validateStationId(firstStationId, lastStationId);

        this.firstStationId = firstStationId;
        this.lastStationId = lastStationId;
    }

    private void validateStationId(Long firstStationId, Long lastStationId) {
        if (firstStationId.equals(lastStationId)) {
            throw new DuplicatedStationIdException();
        }
    }

    public void registerSection(Section newSection, Line line) {
        if (sections.isEmpty()) {
            addSection(newSection, line);
            return;
        }

        validateAlreadyRegisteredSection(newSection);

        if (newSection.hasSameDownStationId(firstStationId)) {
            firstStationId = newSection.getUpStationId();
            addSection(newSection, line);
            return;
        }

        if (newSection.hasSameUpStationId(lastStationId)) {
            lastStationId = newSection.getDownStationId();
            addSection(newSection, line);
            return;
        }

        registerSectionBetweenStations(newSection, line);
    }

    private void validateAlreadyRegisteredSection(Section newSection) {
        sections.stream()
                .filter(section -> section.hasAllSameStations(newSection))
                .findAny()
                .ifPresent(section -> {
                    throw new AlreadyRegisteredStationException();
                });
    }

    private void registerSectionBetweenStations(Section newSection, Line line) {
        Section existingSection = findSectionForRegistration(newSection);

        if (existingSection.hasSameOrLongerDistance(newSection) || existingSection.hasSameOrLongerDuration(newSection)) {
            throw new DoesNotLongerThanExistingSectionException();
        }

        Section additionalSection;
        int additionalSectionDistance = existingSection.getDistance() - newSection.getDistance();
        int additionalSectionDuration = existingSection.getDuration() - newSection.getDuration();

        if (existingSection.hasSameUpStation(newSection)) {
            additionalSection = new Section(newSection.getDownStation(), existingSection.getDownStation(), additionalSectionDistance, additionalSectionDuration);
        } else {
            additionalSection = new Section(existingSection.getUpStation(), newSection.getUpStation(), additionalSectionDistance, additionalSectionDuration);
        }

        sections.remove(existingSection);

        addSection(newSection, line);
        addSection(additionalSection, line);
    }

    private Section findSectionForRegistration(Section newSection) {
        return findSectionOrElseThrow(section -> section.hasOnlyOneSameStation(newSection), new InvalidSectionRegistrationException());
    }

    private Section findSection(Predicate<Section> filterCondition) {
        return findSectionOrElseThrow(filterCondition, new SectionNotFoundException());
    }

    private Section findSectionOrElseThrow(Predicate<Section> filterCondition, BusinessException exception) {
        return sections.stream()
                .filter(filterCondition)
                .findAny()
                .orElseThrow(() -> exception);
    }

    private void addSection(Section newSection, Line line) {
        sections.add(newSection);
        newSection.assignLine(line);
    }

    public List<Section> getSections() {
        List<Section> result = new ArrayList<>();
        Long targetStationId = firstStationId;
        while (targetStationId != null && !targetStationId.equals(lastStationId)) {
            for (Section section : sections) {
                if (section.hasSameUpStationId(targetStationId)) {
                    result.add(section);
                    targetStationId = section.getDownStationId();
                }
            }
        }

        return result;
    }

    public void removeSection(Station station, Line line) {
        validateLineHasOnlyOneSection();

        Long stationId = station.getId();
        if (firstStationId.equals(stationId)) {
            removeFirstStation();
            return;
        }

        if (lastStationId.equals(stationId)) {
            removeLastStation();
            return;
        }

        removeStationBetweenSections(station, line);
    }

    private void removeFirstStation() {
        Section firstSection = findSection(section -> section.hasSameUpStationId(firstStationId));
        Section secondSection = findSection(section -> section.hasSameUpStation(firstSection.getDownStation()));

        sections.remove(firstSection);
        firstStationId = secondSection.getUpStationId();
    }

    private void removeLastStation() {
        Section lastSection = findSection(section -> section.hasSameDownStationId(lastStationId));
        Section prevSection = findSection(section -> section.hasSameDownStation(lastSection.getUpStation()));

        sections.remove(lastSection);
        lastStationId = prevSection.getDownStationId();
    }

    private void removeStationBetweenSections(Station station, Line line) {
        Section prevSection = findSection(section -> section.hasSameDownStation(station));
        Section nextSection = findSection(section -> section.hasSameUpStation(station));

        sections.remove(prevSection);
        sections.remove(nextSection);

        int newSectionDistance = prevSection.getDistance() + nextSection.getDistance();
        int newSectionDuration = prevSection.getDuration() + nextSection.getDuration();
        Section newSection = new Section(prevSection.getUpStation(), nextSection.getDownStation(), newSectionDistance, newSectionDuration);
        registerSection(newSection, line);
    }

    private void validateLineHasOnlyOneSection() {
        if (hasOnlyOneSection()) {
            throw new CanNotDeleteOnlyOneSectionException();
        }
    }

    private boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getLastStationId() {
        return lastStationId;
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
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

    /**
     * 1. 10 이하 : 기본요금
     * 2. 10 초과 ~ 50 이하 : 기본요금 + 100원/5km
     * 3. 50 초과 : 기본요금 + 100원/5km (40) + 100원/8km
     */
    public int calculateFare() {
        int totalDistance = getTotalDistance();

        if (totalDistance <= 10) {
            return BASIC_FEE;
        }

        if (totalDistance <= 50) {
            int remain = totalDistance - 10;
            return BASIC_FEE + calculateOverFee(remain, 5);
        }

        int remain = totalDistance - 50;
        return BASIC_FEE + calculateOverFee(40, 5) + calculateOverFee(remain, 8);
    }

    private int calculateOverFee(int distance, int additionalFeeDistance) {
        return ((distance - 1) / additionalFeeDistance + 1) * 100;
    }
}
