package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

/**
 * TransferLine 객체는 가장 빠른 도착 경로 조회때 환승 타이밍에 맞춰 분리된 노선 입니다.
 *
 * 경로 탐색시 이해하기 힘든 메서드 호출이 이루어지는데 이를 추상화하기 위해 작성 했습니다.
 * [Path -> Sections -> Line -> Sections] -> [Path -> TransferLine]
 * */
public class TransferLine {
    private final List<Section> values;

    public TransferLine() {
        this.values = new ArrayList<>();
    }

    public void add(Section section) {
        values.add(section);
    }

    public int size() {
        return values.size();
    }

    public SubwayDispatchTime firstDispatchTime() {
        Section firstSection = values.get(0);
        return firstSection.dispatchTime();
    }

    public List<Integer> durations() {
        return new Sections(values).durations();
    }
}
