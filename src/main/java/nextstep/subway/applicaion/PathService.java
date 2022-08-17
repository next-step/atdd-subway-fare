package nextstep.subway.applicaion;

import java.util.List;
import java.util.NoSuchElementException;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayFare;
import nextstep.subway.domain.SubwayFarePolicyType;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;

    private MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService,
            MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, PathType.valueOf(type));

        SubwayFarePolicyType subwayFarePolicyType = getFarePolicyType();
        SubwayFare subwayFare = new SubwayFare(subwayFarePolicyType);
        int fareByDistance = subwayFare.calculateFare(path.getShortDistance());
        int discountFare = subwayFare.discountFare(fareByDistance + path.extractExtraCharge());

        return PathResponse.of(path, discountFare);
    }

    private SubwayFarePolicyType getFarePolicyType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return SubwayFarePolicyType.ADULT;
        }
        int age = memberRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(
                        NoSuchElementException::new).getAge();

        if (SubwayFarePolicyType.CHILD.getMinAge() > age) {
            return SubwayFarePolicyType.INFANT;
        }
        if (SubwayFarePolicyType.CHILD.getMaxAge() >= age
                && SubwayFarePolicyType.CHILD.getMinAge() <= age) {
            return SubwayFarePolicyType.CHILD;
        }
        if (SubwayFarePolicyType.YOUTH.getMaxAge() >= age
                && SubwayFarePolicyType.YOUTH.getMinAge() <= age) {
            return SubwayFarePolicyType.YOUTH;
        }

        return SubwayFarePolicyType.ADULT;
    }


}
