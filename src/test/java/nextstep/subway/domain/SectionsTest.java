package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {

    @DisplayName("새로운 구간을 추가한다")
    @Test
    void insertSection() {
        /**
         * line에 대해서 컨트롤하기 어려워서 line에 null을 넣었는데,
         * line의 상태에 대해서 읽어오는 로직이 추가되면서 (getExtraFare()) 테스트를 모두 수정해야 했다
         * 애초에 (당시의) 관심사가 아니라서 null을 넣은 것이 잘못이였을까? 관심사가 아니라면 그냥 emty 객체를 넣어주는 편이 더 낫겠다
         */
        Sections sections = new Sections(new ArrayList<>(List.of(
            new Section(new Line(), new Station("강남역"), new Station("판교역"), 10, 15)
        )));

        sections.add(new Section(new Line(), new Station("판교역"), new Station("양재역"), 2, 3));

        assertThat(sections.getSections())
            .extracting(Section::getDistance)
            .containsExactly(10, 2);

        assertThat(sections.getSections())
            .extracting(Section::getDuration)
            .containsExactly(15, 3);

        assertThat(sections.totalFare(20)).isEqualTo(1350);
    }

    /**
     * 기존 동작을 유지하기 위해 변경된 요구사항에 대해 extraFare()를 분리할까? 아니면 totalFare()에 로직을 포함시킬까?
     *
     * extraFare()를 분리한 경우
     * 장점 : 기존의 코드 / 테스트들에 대해 수정하지 않아도 됨
     * 단점 : totalDiscount()를 호출하고, extraFare()에 대해 추가적으로 호출해야 하므로 호출하는 메서드 수가 많아져서 헷갈림
     *
     * extraFare()라는 메서드로 분리할까 생각했지만, totalFare()를 계산하기 위함이라는 점에서 같다고 생
     * 장점 : 호출하는 입장에서 원하는 바가 뚜렷함
     * 단점 : - 기존의 코드 / 테스트들을 일일이 수정해야 함
     *       - 메서드의 분기가 많아져서 한 메서드 내에서 많은 일을 처리하게 되어 테스트하기 어려워짐
     *       만약 모든 경우의 수에 대하여 테스트하고 싶은 경우, N(분기1의 가짓수) * M(분기2의 가짓수)...의 수에 대하여 테스트하게 되므로 단위테스트 크기가 커짐
     *       (private 메서드는 public 메서드에 종속된다는 관점)
     *
     * 선택한 방향 : totalFare()에 합치기
     * - 기존의 코드 / 테스트들을 수정하더라도 public 메서드 수가 적어져서 하는 일이 명확해지는 장점이 큼
     * - 굳이 N * M 경우에 대하여 테스트할 필요는 없다고 판단. public 메서드 여럿에서 각각 단위테스트하는 것처럼 분기 별로 테스트해서 N + M 횟수만 테스트하면 되지 않나?
     * 이렇게 하면 기존 테스트들은 손 대지 않아도 된다.
     * 대신 이 경우 테스트 메서드 명에 분기하는 경우에 대해 상세히 명세하는 것이 좋겠다는 생각이 들었음
     */
    @DisplayName("구간들 중 추가요금이 있는 노선이 포함될 경우 추가요금이 가장 큰 노선의 추가요금을 포함한 금액을 반환한다")
    @Test
    void totalFare_withExtraFare() {
        int baseFare = 1250;
        int extraFare = 500;
        Line sinbundang = new Line("신분당선", "bg-red-600", extraFare);
        Line bundang = new Line("분당선", "bg-red-600", 300);
        Sections sections = new Sections(
            new Section(sinbundang, new Station("강남역"), new Station("양재역"), 5, 5),
            new Section(bundang, new Station("양재역"), new Station("판교역"), 5, 5)
        );

        assertThat(sections.totalFare(20)).isEqualTo(baseFare + extraFare);
    }

    @DisplayName("총 거리가 10km 이내라면 비용은 1250원으로 고정이다")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void totalFare_under10km(int distance) {
        Sections sections = new Sections(
            new Section(new Line(), new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare(20)).isEqualTo(1250);
    }

    @DisplayName("총 거리가 50km 이내라면 10km 초과 ~ 50km 까지 5km 단위로 100원씩 추가된다")
    @ParameterizedTest
    @CsvSource({"12, 1350", "14, 1350", "15, 1350", "18, 1450"})
    void totalFare_under50km(int distance, int fare) {
        Sections sections = new Sections(
            new Section(new Line(), new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare(20)).isEqualTo(fare);
    }

    @DisplayName("총 거리가 50km 초과시 8km마다 100원씩 추가된다")
    @ParameterizedTest
    @CsvSource({"50, 2050", "55, 2150", "57, 2150", "58, 2150"})
    void totalFare_over50km(int distance, int fare) {
        Sections sections = new Sections(
            new Section(new Line(), new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare(20)).isEqualTo(fare);
    }
}