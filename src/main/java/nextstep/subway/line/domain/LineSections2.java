package nextstep.subway.line.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.CannotAddLineSectionException;
import nextstep.subway.line.exception.CannotRemoveLastLineSectionException;
import nextstep.subway.line.exception.LineSectionAlreadyExistsException;
import nextstep.subway.line.exception.StationNotFoundInLineException;
import nextstep.subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineSections2 {
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "line_id")
  private final List<LineSection2> sections = new ArrayList<>();

  @Builder
  public LineSections2(List<LineSection2> lineSections) {
    this.sections.addAll(lineSections);
  }

  public LineSections2(LineSection2... lineSections) {
    this.sections.addAll(Arrays.asList(lineSections));
  }

  public LineSections2(Station upStation, Station downStation, int distance, int duration) {
    this(LineSection2.of(upStation, downStation, distance, duration));
  }

  public static LineSections2 of(
      Station upStation, Station downStation, int distance, int duration) {
    return new LineSections2(upStation, downStation, distance, duration);
  }

  public int size() {
    return sections.size();
  }

  public boolean isEmpty() {
    return sections.isEmpty();
  }

  public LineSection2 getFirst() {
    return sections.get(0);
  }

  public LineSection2 getLast() {
    return sections.get(sections.size() - 1);
  }

  public void add(LineSection2 lineSection) {
    validateAdd(lineSection);
    if (isEmpty()) {
      sections.add(lineSection);
      return;
    }
    if (isPrepend(lineSection)) {
      sections.add(0, lineSection);
      return;
    }
    if (isAppend(lineSection)) {
      sections.add(lineSection);
      return;
    }
    if (insertUp(lineSection)) {
      return;
    }
    if (insertDown(lineSection)) {
      return;
    }
    throw new CannotAddLineSectionException();
  }

  private boolean insertUp(LineSection2 lineSection) {
    OptionalInt optionalIndex = indexOfSplitUp(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private boolean insertDown(LineSection2 lineSection) {
    OptionalInt optionalIndex = indexOfSplitDown(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private void insert(LineSection2 lineSection, int index) {
    LineSection2 splitTargetSection = sections.remove(index);
    List<LineSection2> splitSections = splitTargetSection.split(lineSection);
    sections.addAll(index, splitSections);
  }

  private OptionalInt indexOfSplitUp(LineSection2 lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitUp(lineSection))
        .findFirst();
  }

  private OptionalInt indexOfSplitDown(LineSection2 lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitDown(lineSection))
        .findFirst();
  }

  private boolean isPrepend(LineSection2 lineSection) {
    return getFirst().canPrepend(lineSection);
  }

  private boolean isAppend(LineSection2 lineSection) {
    return getLast().canAppend(lineSection);
  }

  private void validateAdd(LineSection2 lineSection) {
    if (containsBothStations(lineSection)) {
      throw new LineSectionAlreadyExistsException();
    }
  }

  private boolean containsBothStations(LineSection2 lineSection) {
    List<Station> stations = getStations();
    return stations.stream().anyMatch(it -> it.isSame(lineSection.getUpStation()))
        && stations.stream().anyMatch(it -> it.isSame(lineSection.getDownStation()));
  }

  public void addAll(LineSections2 lineSections) {
    lineSections.sections.forEach(this::add);
  }

  public List<Station> getStations() {
    if (sections.isEmpty()) {
      return Collections.emptyList();
    }
    List<Station> stations =
        sections.stream().map(LineSection2::getUpStation).collect(Collectors.toList());
    stations.add(getLast().getDownStation());
    return Collections.unmodifiableList(stations);
  }

  public void remove(Station station) {
    validateRemove(station);
    if (removeTerminal(station)) {
      return;
    }
    if (removeMiddle(station)) {
      return;
    }
    throw new IllegalArgumentException("역 #" + station.getId() + " 를 제거할 수 없습니다.");
  }

  private boolean removeMiddle(Station station) {
    OptionalInt optionalIndex = indexOfSectionContaining(station);
    if (optionalIndex.isEmpty()) {
      return false;
    }
    int index = optionalIndex.getAsInt();
    LineSection2 upSection = sections.remove(index);
    LineSection2 downSection = sections.remove(index);
    LineSection2 mergedSection = upSection.merge(downSection);
    sections.add(index, mergedSection);
    return true;
  }

  private OptionalInt indexOfSectionContaining(Station station) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).contains(station))
        .findFirst();
  }

  private boolean removeTerminal(Station station) {
    if (getFirst().getUpStation().isSame(station)) {
      sections.remove(0);
      return true;
    }
    if (getLast().getDownStation().isSame(station)) {
      sections.remove(sections.size() - 1);
      return true;
    }
    return false;
  }

  private void validateRemove(Station station) {
    if (!getStations().contains(station)) {
      throw new StationNotFoundInLineException(station.getId());
    }
    if (size() <= 1) {
      throw new CannotRemoveLastLineSectionException();
    }
  }
}
