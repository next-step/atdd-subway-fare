package nextstep.study;

import org.junit.jupiter.api.Test;

public class OverrideTest {
    interface TestPolicy {
        void calculateFare(TestPath testPath);
    }

    static class TestPath {}

    static abstract class AdditionalPolicy implements TestPolicy {
        private final TestPolicy next;

        AdditionalPolicy(TestPolicy next) {
            this.next = next;
        }

        @Override
        public void calculateFare(TestPath testPath) {
            next.calculateFare(testPath);
            afterCalculated();
        }

        abstract protected void afterCalculated();
    }

    static class BasicTestPolicy implements TestPolicy {
        @Override
        public void calculateFare(TestPath testPath) {
            System.out.println("기본 정책의 calculateFare() 메서드 호출");
        }
    }

    static class DistanceTestPolicy extends AdditionalPolicy {
        DistanceTestPolicy(TestPolicy next) {
            super(next);
        }

        @Override
        protected void afterCalculated() {
            System.out.println("거리 정책의 afterCalculated() 메서드 호출");
        }
    }

    static class LineTestPolicy extends AdditionalPolicy {
        LineTestPolicy(TestPolicy next) {
            super(next);
        }

        @Override
        protected void afterCalculated() {
            System.out.println("노선 정책의 afterCalculated() 메서드 호출");
        }
    }

    static class DiscountTestPolicy extends AdditionalPolicy {
        DiscountTestPolicy(TestPolicy next) {
            super(next);
        }

        @Override
        protected void afterCalculated() {
            System.out.println("할인 정책의 afterCalculated() 메서드 호출");
        }
    }

    @Test
    void test() {
        TestPolicy testPolicy = new DiscountTestPolicy(new LineTestPolicy(new BasicTestPolicy()));
        testPolicy.calculateFare(new TestPath());
    }
}
