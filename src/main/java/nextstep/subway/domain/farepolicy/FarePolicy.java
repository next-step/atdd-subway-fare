package nextstep.subway.domain.farepolicy;

public interface FarePolicy {
    boolean isSupported();
    long getFare();
}
