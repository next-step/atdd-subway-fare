package nextstep.subway.util;

public class Adult extends Age {

    @Override
    int discount(int fare) {
        return fare;
    }
}
