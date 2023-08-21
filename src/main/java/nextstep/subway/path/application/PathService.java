package nextstep.subway.path.application;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayMap;
import nextstep.subway.path.domain.discount.DefaultDiscountPolicy;
import nextstep.subway.path.domain.discount.DiscountPolicy;
import nextstep.subway.path.domain.discount.age.AgeType;
import nextstep.subway.path.domain.discount.age.ChildrenDiscountPolicy;
import nextstep.subway.path.domain.discount.age.DefaultAgeDiscountPolicy;
import nextstep.subway.path.domain.discount.age.TeenagerDiscountPolicy;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.UserDto;
import nextstep.subway.path.exception.SameSourceAndTargetStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;
    private final DistanceFarePolicies distanceFarePolicies;
    private final LineFarePolicy lineFarePolicy;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, MemberRepository memberRepository, DistanceFarePolicies distanceFarePolicies, LineFarePolicy lineFarePolicy) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
        this.distanceFarePolicies = distanceFarePolicies;
        this.lineFarePolicy = lineFarePolicy;
    }

    public PathResponse searchPath(UserDto userDto, Long source, Long target, String type) {
        Path path = findPath(source, target, type);

        // user의 role이 unknown이면 별다른 할인 정책이 적용되지 않는다.
        if (RoleType.isUnknown(userDto.getRole())) {
            return PathResponse.of(path, distanceFarePolicies, lineFarePolicy, new DefaultDiscountPolicy());
        }

        // 회원인 경우에는 age에 따른 할인 정책이 적용된다.
        Member member = findMember(userDto.getEmail());

        // age 기준에 따라 할인 정책을 찾는다.
        Integer age = member.getAge();
        DiscountPolicy discountPolicy = classifyDiscountPolicy(age);

        return PathResponse.of(path, distanceFarePolicies, lineFarePolicy, discountPolicy);
    }

    private Path findPath(Long source, Long target, String type) {
        validateSourceAndTargetId(source, target);

        Station sourceStation = findStation(source);
        Station targetStation = findStation(target);

        List<Line> lines = lineRepository.findAll();
        SubwayMap subwayMap = new SubwayMap(lines, type);

        return subwayMap.findPath(sourceStation, targetStation);
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private DiscountPolicy classifyDiscountPolicy(Integer age) {
        DiscountPolicy discountPolicy = new DefaultAgeDiscountPolicy();
        if (AgeType.isTeenager(age)) {
            discountPolicy = new TeenagerDiscountPolicy();
        }

        if (AgeType.isChildren(age)) {
            discountPolicy = new ChildrenDiscountPolicy();
        }

        return discountPolicy;
    }

    private void validateSourceAndTargetId(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetStationException();
        }
    }

    private Station findStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(StationNotFoundException::new);
    }

    public void validatePathConnection(Long source, Long target) {
        findPath(source, target, "DISTANCE");
    }
}
