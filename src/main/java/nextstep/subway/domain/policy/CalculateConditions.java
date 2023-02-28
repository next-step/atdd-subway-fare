package nextstep.subway.domain.policy;

import nextstep.subway.domain.Line;

import java.util.List;
import java.util.Objects;

public class CalculateConditions {
    private Integer distance;
    private Integer age;
    private List<Line> lines;

    private CalculateConditions(CalculateConditionBuilder builder) {
        this.distance = builder.distance;
        this.age = builder.age;
        this.lines = builder.lines;
    }

    public static CalculateConditionBuilder builder(int distance) {
        return new CalculateConditionBuilder(distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        if(Objects.isNull(age)){
            return 20;
        }
        return age;
    }

    public List<Line> getLines() {
        return lines;
    }

    public static class CalculateConditionBuilder {
        private Integer distance;
        private Integer age;
        private List<Line> lines;

        public CalculateConditionBuilder(int distance) {
            this.distance = distance;
        }

        public CalculateConditionBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public CalculateConditionBuilder lines(List<Line> lines) {
            this.lines = lines;
            return this;
        }

        public CalculateConditions build() {
            return new CalculateConditions(this);
        }
    }

}
