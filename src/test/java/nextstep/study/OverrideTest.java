package nextstep.study;

public class OverrideTest {
    interface TestPolicy {
        int calculateFare(TestPath testPath);
    }

    static class TestPath {}

    static abstract class AdditionalPolicy implements TestPolicy {
        private final TestPolicy next;

        AdditionalPolicy(TestPolicy next) {
            this.next = next;
        }

        @Override
        public int calculateFare(TestPath testPath) {
            int fare = next.calculateFare(testPath);
            return afterCalculated(fare);
        }

        abstract protected int afterCalculated(int fare);
    }
}
