package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.auth.principal.LoginMember;
import nextstep.auth.userdetails.UserDetails;
import nextstep.line.Line;
import nextstep.line.LineRepository;
import nextstep.line.section.Section;
import nextstep.member.domain.AgeRange;
import nextstep.path.fare.FareCalculator;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import nextstep.station.StationResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Path {
    private List<String> stationIds;
    private int distance;
    private int duration;

    public PathResponse toResponse(StationRepository stationRepository, LineRepository lineRepository, LoginMember loginMember) {
        List<Station> stationEntitys = stationIds.stream()
                .map(Long::parseLong)
                .map(stationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<StationResponse> stations = stationEntitys.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        List<Line> lines = lineRepository.findAll();
        Set<Line> linesInPath = findLinesInPath(stationEntitys, lines);
        AgeRange ageRange = AgeRange.findByAge(loginMember.getAge());
        FareCalculator fareCalculator = new FareCalculator(distance, linesInPath, ageRange);
        int fare = fareCalculator.calculate();

        return new PathResponse(stations, distance, duration, fare);
    }

    private Set<Line> findLinesInPath(List<Station> path, List<Line> allLines) {
        Set<Line> linesInPath = new HashSet<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Station upstation = path.get(i);
            Station downstation = path.get(i + 1);

            for (Line line : allLines) {
                for (Section section : line.getSections()) {
                    if (section.isUpstation(upstation) && section.isDownstation(downstation)) {
                        linesInPath.add(line);
                        break;
                    }
                }
            }
        }

        return linesInPath;
    }
}
