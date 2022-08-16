package nextstep.subway.applicaion;

import java.util.List;
import nextstep.member.domain.Guest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.policy.FarePolicyRegistry;
import nextstep.subway.domain.policy.age.AgeFarePolicy;
import nextstep.subway.domain.policy.distance.DistanceFarePolicy;
import nextstep.subway.domain.policy.surcharge.SurchargePolicy;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(Long source, Long target, String type, String email) {
        int age = getMemberAgeByEmail(email);

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        return PathResponse.of(
                getPathByType(upStation, downStation, lines, type),
                getPathByShortestDistanceFare(upStation, downStation, lines, age)
        );
    }

    private int getMemberAgeByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElse(Guest.guestMember);
        return member.getAge();
    }

    private Path getPathByType(Station upStation, Station downStation, List<Line> lines, String type) {
        SubwayMap subwayMap = new SubwayMap(lines, PathType.valueOf(type));
        return subwayMap.findPath(upStation, downStation);
    }


    private int getPathByShortestDistanceFare(Station upStation, Station downStation, List<Line> lines, int age) {
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);
        Path path = subwayMap.findPath(upStation, downStation);
        int distance = path.extractDistance();

        FarePolicyRegistry handler = new FarePolicyRegistry();
        handler.addPolicy(new DistanceFarePolicy());
        handler.addPolicy(new SurchargePolicy());
        handler.addPolicy(new AgeFarePolicy());

        return handler.calculate(age, distance, 0, path);
    }
}
