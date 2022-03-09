package nextstep.subway.domain;

public interface Policy {

    void setNextPolicy(Policy nextPolicy);
    int getPolicyFare(int fare, int age);
}
