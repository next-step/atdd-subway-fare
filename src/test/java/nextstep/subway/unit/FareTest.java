package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    public static final Station 양재역 = new Station("양재역");
    public static final Station 교대역 = new Station("교대역");
    public static final Station 남부터미널역 = new Station("남부터미널역");

    Line 삼호선;

    Section 교대_남부터미널_구간;
    Section 남부터미널_양재_구간;

    @BeforeEach
    void setUp() {
        삼호선 = new Line("이호선", "orange", 500);
        교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);
        남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 10);
        삼호선.getSections().add(교대_남부터미널_구간);
        삼호선.getSections().add(남부터미널_양재_구간);
    }

    @Test
    void calculate() {
        List<Section> sections = List.of(교대_남부터미널_구간, 남부터미널_양재_구간);
        assertThat(Fare.calculate(new Sections(sections)).getFare()).isEqualTo(1750);
    }
}
