package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.map.Groups;

@DisplayName("Group Test")
public class GroupsTest {
    /**
     * 임시 Key 값을 사용해 List 로 그룹화 한다.
     * Map 과 다른점은 Key-Value 를 한 쌍으로 저장하지 않고
     * push 메서드의 행동을 분기하기 위해 임시로 사용한다.
     *
     * 바로 전 push 메서드 호출 Key 값과 일치 -> 바로 전 push 메서드 호출때의 element 가 들어간 그룹에 추가 된다.
     * 바로 전 push 메서드 호출 Key 값과 불일치 -> 새로운 그룹이 생성 된다.
     * */
    @DisplayName("같은 Key 값이라도 이전 Key와 일치 하지 않으면 다른 그룹으로 추가 한다.")
    @Test
    void pushTest() {
        Groups<Integer, String> groups = new Groups<>();
        groups.put(1, "1");
        groups.put(1, "2");
        groups.put(2, "3");
        groups.put(1, "4");

        assertThat(groups.size()).isEqualTo(3);
    }
}
