package nextstep.path.dto;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Sections;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;

import java.util.ArrayList;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;

public class Path {
    private final Member member;
    private final List<Line> lines;
    private final List<Station> stations;
    private final Sections sections;
    private final Long totalDistance;
    private final Long totalDuration;
    private Long totalPrice;

    public Path(Member member, List<Line> lines, List<Station> stations, Sections sections, Long totalDistance, Long totalDuration, Long totalPrice) {
        this.member = member;
        this.lines = lines;
        this.stations = stations;
        this.sections = sections;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    public static Path of(final Member member, final List<Line> lines, final List<Station> stations, final Sections sections) {
        Long totalDistance = sections.getTotalDistance();
        Long totalDuration = sections.getTotalDuration();

        return new Path(member, lines, stations, sections, totalDistance, totalDuration, null);
    }

    public PathResponse createPathResponse() {
        if (stations == null || stations.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : stations) {
            stationResponses.add(StationResponse.of(station.getId(), station.getName()));
        }

        return PathResponse.of(stationResponses, totalDistance, totalDuration, totalPrice);
    }

    public Member getMember() {
        return member;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Sections getSections() {
        return sections;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}

