package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

public class TransferLineBuilder {
    private final List<TransferLine> values;
    private Line previousLine;

    private TransferLineBuilder() {
        this.values = new ArrayList<>();
    }

    public static List<TransferLine> create(Sections sections) {
        TransferLineBuilder transferLines = new TransferLineBuilder();
        sections.forEach(
            eachSection -> transferLines.put(eachSection.getLine(), eachSection)
        );
        return transferLines.values;
    }

    public void put(Line key, Section section) {
        if (notEqualsPreviousKey(key)) {
            values.add(new TransferLine());
        }
        putAtLastTransferLine(section);
        previousLine = key;
    }

    private boolean notEqualsPreviousKey(Line line) {
        return Objects.isNull(line) || Objects.isNull(previousLine) || !line.equals(previousLine);
    }

    private void putAtLastTransferLine(Section section) {
        TransferLine lastTransferLine = values.get(values.size() - 1);
        lastTransferLine.add(section);
    }

    public int size() {
        return values.size();
    }
}
