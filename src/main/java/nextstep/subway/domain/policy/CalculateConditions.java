package nextstep.subway.domain.policy;

public class CalculateConditions {
    private Integer distance;
    private Integer age;

    private CalculateConditions(CalculateConditionBuilder builder) {
        this.distance = builder.distance;
        this.age = builder.age;
    }

    public static CalculateConditionBuilder builder(int distance){
        return new CalculateConditionBuilder(distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }


    public static class CalculateConditionBuilder{
        private Integer distance;
        private Integer age;

        public CalculateConditionBuilder(int distance) {
            this.distance = distance;
        }

        public CalculateConditionBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public CalculateConditions build(){
            return new CalculateConditions(this);
        }
    }

}
