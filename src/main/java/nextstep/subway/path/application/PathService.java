package nextstep.subway.path.application;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayMap;
import nextstep.subway.path.domain.policy.discount.DefaultDiscountPolicy;
import nextstep.subway.path.domain.policy.discount.DiscountPolicy;
import nextstep.subway.path.domain.policy.discount.AgeType;
import nextstep.subway.path.domain.policy.discount.ChildrenDiscountPolicy;
import nextstep.subway.path.domain.policy.discount.DefaultAgeDiscountPolicy;
import nextstep.subway.path.domain.policy.discount.TeenagerDiscountPolicy;
import nextstep.subway.path.domain.policy.fare.FarePolicy;
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
    private final FarePolicy farePolicy;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, MemberRepository memberRepository, FarePolicy farePolicy) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
        this.farePolicy = farePolicy;
    }

    public PathResponse searchPath(UserDto userDto, Long source, Long target, String type) {
        Path path = findPath(source, target, type);
        int totalFare = path.calculateFare(farePolicy);

        if (userDto.isUnknown()) {
            DiscountPolicy defaultDiscountPolicy = new DefaultDiscountPolicy();
            return PathResponse.of(path, defaultDiscountPolicy.discount(totalFare));
        }

        Member member = findMember(userDto.getEmail());
        DiscountPolicy discountPolicy = classifyDiscountPolicy(member.getAge());

        return PathResponse.of(path, discountPolicy.discount(totalFare));
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
        if (AgeType.isTeenager(age)) {
            return new TeenagerDiscountPolicy();
        }

        if (AgeType.isChildren(age)) {
            return new ChildrenDiscountPolicy();
        }

        return new DefaultAgeDiscountPolicy();
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
