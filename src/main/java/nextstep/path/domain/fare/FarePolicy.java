package nextstep.path.domain.fare;

@FunctionalInterface
public interface FarePolicy {
    int apply(int beforeFare);
}
