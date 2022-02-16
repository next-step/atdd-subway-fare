package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jdk.vm.ci.meta.Local;

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
                        new Section(section.getLine(),
                                    it.getUpStation(),
                                    section.getUpStation(),
                                    it.getDistance() - section.getDistance(),
                                    it.getDuration() - section.getDuration()));
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
                        new Section(section.getLine(),
                                    section.getDownStation(),
                                    it.getDownStation(),
                                    it.getDistance() - section.getDistance(),
                                    it.getDuration() - section.getDuration()));
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

    private void addNewSectionForDelete(Optional<Section> optionalUpSection, Optional<Section> optionalDownSection) {
        if (optionalUpSection.isPresent() && optionalDownSection.isPresent()) {
            Section upSection = optionalUpSection.get();
            Section downSection = optionalDownSection.get();

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

    public LocalDateTime arrivalTime(LocalDateTime startTime) {
        /**
         1. 강남역에서 선릉역으로 가는 방향으로 강남역에 가장 빨리 도착하는 시간 찾기
         10:00 기준 강남역->선릉역 방향 // 강남역에 가장 빨리 도착하는 시간은 10:03
         10:00에 서초역에서 선릉역 방향으로 출발한 지하철
         3분 소요시간 후 강남역에 도착하기 때문에 10:03
         2. 강남역에서 선릉역 까지 소요시간 계산
         강남역에서 선릉역까지 소요시간은 7분
         * */
        // 1. 첫번째 역에서 지정한 시간에 출발해 두번째 역에 가장 빨리 도착하는 시간 찾기
        // -- 첫차 시간부터 입력 받은 시간보다 클때까지 소요 시간을 더한다.
        // 2. 두번째 역에서 마지막 까지의 역을 모두 합산하기
        Section firstSection = sections.get(0);
        LocalTime lineStartTime = firstSection.getLine()
                                              .getStartTime();
        LocalTime lineEndTime = firstSection.getLine()
                                            .getEndTime();
        LocalTime lineIntervalTime = firstSection.getLine()
                                                 .getIntervalTime();


        //LocalDateTime firstSectionStartTime = firstSectionStartTime();
        //return firstSectionStartTime.plusMinutes(totalDuration());
        return null;
    }

    private LocalDateTime firstSectionStartTime(LocalDateTime startTime, LocalTime lineStartTime,
                                                LocalTime lineEndTime, LocalTime lineIntervalTime) {
        /*
        LocalDateTime lineEndDateTime = LocalDateTime.of(startTime.toLocalDate(), lineEndTime);
        LocalDateTime firstSectionStartTime = LocalDateTime.of(startTime.toLocalDate(), lineStartTime);
        while (firstSectionStartTime.isBefore(startTime)) {
            firstSectionStartTime = plusTime(firstSectionStartTime, lineIntervalTime);
        }
        return firstSectionStartTime;

         */
        return  null;
    }

    private LocalDateTime plusTime(LocalDateTime localDateTime, LocalTime time, LocalDateTime lineEndDateTime) {
        LocalDateTime nextTime = localDateTime.plusHours(time.getHour())
                                              .plusMinutes(time.getMinute())
                                              .plusHours(time.getSecond());
        return null;
    }

    private void addSectionTime(LocalDateTime ongoingTime, LocalTime duration) {
        //LocalDateTime nextTime = ongoingTime.plusMinutes(duration)
    }
}
