package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

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
