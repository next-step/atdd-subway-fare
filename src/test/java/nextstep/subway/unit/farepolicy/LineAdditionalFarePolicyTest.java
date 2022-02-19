package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.farepolicy.LineAdditionalFarePolicy;

@DisplayName("라인 추가 요금 정책 테스트")
@ExtendWith(MockitoExtension.class)
public class LineAdditionalFarePolicyTest {
    @Mock private Section section1;
    @Mock private Section section2;
    @Mock private Section section3;

    @DisplayName("거쳐간 라인 중 가장 큰 추가 요금을 반환한다.")
    @Test
    void calculate() {
        when(section1.getAdditionalFare()).thenReturn(10);
        when(section2.getAdditionalFare()).thenReturn(20);
        when(section3.getAdditionalFare()).thenReturn(30);
        Path path = new Path(
            new Sections(Arrays.asList(
                section1, section2, section3
            ))
        );

        assertThat(new LineAdditionalFarePolicy().calculate(path)).isEqualTo(30);
    }
}
