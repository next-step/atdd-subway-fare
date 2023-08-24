package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.discount.DiscountPolicy;
import nextstep.subway.path.domain.fare.distance.DistanceFarePolicies;
import nextstep.subway.path.domain.fare.line.LineFarePolicy;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

/**
 * # 책임 연쇄 패턴을 도입하기 위한 방안을 생각해보자.
 * ## 정책
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
public class PathVer2 {
    private final Lines lines;
    private final Sections sections;

    public PathVer2(Lines lines, Sections sections) {
        this.lines = lines;
        this.sections = sections;
    }

    public Set<Line> getLines() {
        return lines.getLines();
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
}
