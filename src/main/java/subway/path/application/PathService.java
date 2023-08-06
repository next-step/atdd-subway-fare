package subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.auth.principal.UserPrincipal;
import subway.line.application.LineService;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.member.application.MemberService;
import subway.member.domain.Member;
import subway.path.application.dto.PathFinderRequest;
import subway.path.application.dto.PathRetrieveRequest;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.application.path.PathFinder;
import subway.path.application.path.PathFinderFactory;
import subway.path.domain.Path;
import subway.path.domain.PathRetrieveType;
import subway.station.application.StationService;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final MemberService memberService;

    public void validPath(Station sourceStation, Station targetStation) {
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);
        PathFinder pathFinder = PathFinderFactory.createFinder(PathRetrieveType.DISTANCE);
        PathFinderRequest pathFinderRequest = PathFinderRequest.builder()
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .sections(sections)
                .build();
        pathFinder.findPath(pathFinderRequest);
    }

    public PathRetrieveResponse getPath(PathRetrieveRequest request) {
        long memberAge = getMemberFromPrincipal(request.getPrincipal());
        return getPath(request.getSource(), request.getTarget(), request.getType(), memberAge);
    }

    private PathRetrieveResponse getPath(long sourceStationId,
                                         long targetStationId,
                                         PathRetrieveType type,
                                         long memberAge) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);

        PathFinder pathFinder = PathFinderFactory.createFinder(type);
        PathFinderRequest pathFinderRequest = PathFinderRequest.builder()
                .memberAge(memberAge)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .sections(sections)
                .build();
        Path path = pathFinder.findPath(pathFinderRequest);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(path.getStations()))
                .distance(path.getTotalDistance())
                .duration(path.getTotalDuration())
                .fare(path.getTotalFare())
                .build();
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getLineSections().getSections().stream())
                .collect(Collectors.toList());
    }

    private long getMemberFromPrincipal(UserPrincipal principal) {
        if (principal != null) {
            Member member = memberService.getMember(principal);
            return member.getAge();
        }
        return 0;
    }
}
