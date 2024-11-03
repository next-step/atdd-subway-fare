package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberType;
import nextstep.subway.domain.line.Line;

import java.util.List;

public class Fare {
    private static final long DEFAULT_FARE = 1250L;

    private Long fare;

    public Fare() {
        this.fare = DEFAULT_FARE;
    }

    public Fare(Long fare) {
        this.fare = fare;
    }

    public void calculateDistanceBasedFare(long distance) {
        long totalFare = this.fare;
        if (distance <= 10) {
            return;
        }

        if (distance <= 50) {
            long until50km = distance - 10;
            this.fare = totalFare + (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);
            return;
        }

        // 50km까지 요금
        long until50km = 40;
        totalFare += (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);

        // 50km 초과 요금
        long over50km = distance - 50;
        this.fare = totalFare + (long) ((Math.ceil((over50km - 1) / 8) + 1) * 100);
    }

    public void calculateDistanceBasedFare(long distance, List<Line> lines) {
        boolean hasAdditionalFare = lines.stream()
                .anyMatch(line -> line.getAdditionalFare() > 0L);

        if (hasAdditionalFare) {
            calculateHighestAdditionalFare(lines);
        } else {
            calculateDistanceBasedFare(distance);
        }
    }

    public void calculateDistanceBasedFare(long distance, List<Line> lines, Member member) {
        calculateDistanceBasedFare(distance, lines);
        calculateAgeDiscountFare(member);
    }

    private void calculateHighestAdditionalFare(List<Line> lines) {
        Long highestAdditionalFare = lines.stream()
                .map(Line::getAdditionalFare)
                .max(Long::compare)
                .get();

        this.fare += highestAdditionalFare;
    }

    private void calculateAgeDiscountFare(Member member) {
        if (member == null || member.getAge() == null) {
            return;
        }
        Integer age = member.getAge();
        MemberType memberType = MemberType.getMemberType(age);

        if (memberType.equals(MemberType.TEENAGER)) {
            this.fare = (long) ((this.fare - 350) * 0.8);
        } else if (memberType.equals(MemberType.CHILDREN)) {
            this.fare = (long) ((this.fare - 350) * 0.5);
        }
    }

    public Long getFare() {
        return fare;
    }
}
