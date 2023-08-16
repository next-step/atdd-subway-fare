package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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
        삼호선 = new Line("이호선", "orange", 100);
        교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);
        남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 10);
        삼호선.getSections().add(교대_남부터미널_구간);
        삼호선.getSections().add(남부터미널_양재_구간);
    }

    @Test
    @DisplayName("추가 요금 계산")
    void calculate() {
        List<Section> sections = List.of(교대_남부터미널_구간, 남부터미널_양재_구간);
        assertThat(Fare.calculate(new Sections(sections)).getFare()).isEqualTo(1350);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "5,1350",
            "6,500",
            "12,500",
            "13,800",
            "18,800",
            "19,1350"
    })
    @DisplayName("할인 요금 계산")
    void discount(int age, int expected) {
        List<Section> sections = List.of(교대_남부터미널_구간, 남부터미널_양재_구간);
        Member member = new Member("test1", "1234", age);

        assertThat(Fare.calculate(new Sections(sections), member).getFare()).isEqualTo(expected);
    }
}
