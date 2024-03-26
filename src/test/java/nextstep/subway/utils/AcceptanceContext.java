package nextstep.subway.utils;

import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Profile("test")
@Component
public class AcceptanceContext {
    private Map<String, Station> stationStore = new HashMap<>();
    private Map<String, Line> lineStore = new HashMap<>();
    private Map<String, MemberResponse> memberStore = new HashMap<>();
    private Map<String, String> memberTokenStore = new HashMap<>();

    public void putStation(String name, Station station) {
        stationStore.put(name, station);
    }

    public void putLine(String name, Line line) {
        lineStore.put(name, line);
    }
    public void putMember(String email, MemberResponse member) {
        memberStore.put(email, member);
    }

    public void addSection(String line, Section newSection) {
        lineStore.get(line).addSection(newSection);
    }

    public Line findLine(String name) {
        return lineStore.get(name);
    }

    public Station findStation(String name) {
        return stationStore.get(name);
    }

    public Station findStationById(Long id) {
        return stationStore.values()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public MemberResponse findMember(String email) {
        return memberStore.values()
                .stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst()
                .orElseThrow();
    }

    public void putMemberToken(String email, String token) {
        memberTokenStore.put(email, token);
    }

    public String findMemberToken(String email) {
        return memberTokenStore.get(email);
    }
}
