package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    /**
     * Sections는 구간을 가지고 있으므로 totalFare에 대한 책임은 거기에 묻고,
     * Path는 사용자가 요청한 것이기 때문에 discount 정책에 대한 책임은 Path에 묻는다
     *
     * 호출하는 depth가 깊어지면 번거롭다고 생각했는데, 이런 식으로 조건이 추가됨에 따라
     * 기능들을 격리시킬 수 있는 decoupling할 수 있다는 장점이 될 수 있다는 건 생각 못했음
     * depth가 깊어지는 것에 대해 실용적이지 않다거나, 너무 부정적으로 생각할 필요는 없다고 생각하게 됨
     */
    public int extractFare(int age) {
        return AgeDiscountPolicy.discountFromAge(age, sections.totalFare());
    }

    public int extractFare() {
        return sections.totalFare();
    }
}
