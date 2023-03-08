package nextstep.study;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class LocalTimeTest {

    @Test
    void now() {
        LocalTime now = LocalTime.now();
        System.out.println(now);
    }
}
