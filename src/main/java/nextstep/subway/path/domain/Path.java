package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.chainofresponsibility.FarePolicy;
import nextstep.subway.path.domain.discount.DiscountPolicy;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Lines lines;
    private final Sections sections;

    public Path(Lines lines, Sections sections) {
        this.lines = lines;
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return sections.calculateTotalDistance();
    }

    public int getTotalDuration() {
        return sections.calculateTotalDuration();
    }

    public int calculateFare(DistanceFarePolicies distanceFarePolicies, LineFarePolicy lineFarePolicy, DiscountPolicy discountPolicy) {
        int fare = distanceFarePolicies.calculateFare(getTotalDistance());
        int additionalFare = lineFarePolicy.calculateAdditionalFare(lines);

        int totalFare = fare + additionalFare;
        return discountPolicy.discount(totalFare);
    }

    /**
     * # 책임 연쇄 패턴을 도입하기 위한 방안을 생각해보자.
     * ## 요금 정책
     * - 거리에 따른 요금 정책
     * - 노선 추가 요금 정책
     * ---
     * ## 할인 정책
     * - 할인 없음
     * - 나이에 따른 할인 정책
     * ---
     * ## 참고
     * - <a href="https://refactoring.guru/ko/design-patterns/chain-of-responsibility">책임연쇄패턴</a>
     * - 오브젝트 11장 (ebook 기준 398쪽)
     */
    public int calculateFareVer2(FarePolicy farePolicy) {
        return farePolicy.calculateFare(this);
    }
}
