package nextstep.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.auth.principal.LoginMember;
import nextstep.line.Line;
import nextstep.line.Lines;
import nextstep.line.section.Section;
import nextstep.member.domain.AgeRange;
import nextstep.path.fare.FareCalculator;
import nextstep.station.Station;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Path {
    private List<Section> sections;
    private List<Station> stations;
    private int distance;
    private int duration;

    public int getFare(Lines lines, LoginMember loginMember) {
        Set<Line> linesInPath = lines.findLinesInPath(stations);
        AgeRange ageRange = AgeRange.findByAge(loginMember.getAge());
        FareCalculator fareCalculator = new FareCalculator(distance, linesInPath, ageRange);
        return fareCalculator.calculate();
    }

    public LocalTime getArrivalTime(LocalTime departureTime) {
        ArrivalTimeCalculator arrivalTimeCalculator = new ArrivalTimeCalculator(sections, departureTime);
        return arrivalTimeCalculator.calculate();
    }
}
