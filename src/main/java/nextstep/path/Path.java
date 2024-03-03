package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.auth.principal.LoginMember;
import nextstep.auth.userdetails.UserDetails;
import nextstep.line.Line;
import nextstep.line.LineRepository;
import nextstep.line.Lines;
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
    private List<Station> stations;
    private int distance;
    private int duration;

    public int getFare(Lines lines, LoginMember loginMember) {
        Set<Line> linesInPath = lines.findLinesInPath(stations);
        AgeRange ageRange = AgeRange.findByAge(loginMember.getAge());
        FareCalculator fareCalculator = new FareCalculator(distance, linesInPath, ageRange);
        return fareCalculator.calculate();
    }

}
