package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FareCalculatorTest {

    @Autowired
    FareCalculator fareCalculator;

    Line 일호선;
    Line 이호선;

    @BeforeEach
    void setUp() {
        //추가요금 0원
        일호선 = new Line("1호선", "blue");
        ReflectionTestUtils.setField(일호선, "id", 1L);

        //추가요금 100원
        이호선 = new Line("2호선", "green");
        ReflectionTestUtils.setField(이호선, "id", 2L);
    }

    @DisplayName("요금 기본 성인")
    @Test
    void 요금_확인_기본_성인() {
        //given
        Set<Line> lines = new HashSet<>();
        lines.add(일호선);
        //when
        int total = fareCalculator.getTotalFare(lines, 8, 30);
        //then
        //기본요금 1250
        assertThat(total).isEqualTo(1250);
    }

    @DisplayName("요금 10km 이상 소아")
    @Test
    void 요금_확인_10km이상_소아() {
        //given
        Set<Line> lines = new HashSet<>();
        lines.add(일호선);
        //when
        int total = fareCalculator.getTotalFare(lines, 16, 8);
        //then
        //(기본요금 1250 + 구간추가요금 200 - 공제액 350) * (1 - 0.5)
        assertThat(total).isEqualTo(550);
    }

    @DisplayName("요금 노선추가 50km이상 청소년")
    @Test
    void 요금_노선추가_50km이상_청소년() {
        //given
        Set<Line> lines = new HashSet<>();
        lines.add(일호선);
        lines.add(이호선);
        //when
        int total = fareCalculator.getTotalFare(lines, 60, 15);
        //then
        //(기본요금 1250 + 노선추가요금 100 + 구간추가요금 800 + 200 - 공제액 350) * (1 - 0.2)
        assertThat(total).isEqualTo(1600);
    }

}
