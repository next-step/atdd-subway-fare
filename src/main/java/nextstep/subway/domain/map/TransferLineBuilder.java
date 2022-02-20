package nextstep.subway.domain.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

/**
 * TransferLineBuilder 객체는 TransferLine 생성 과정을 추상화한 객체 입니다.
 * 환승 타이밍을 기준으로 같은 노선을 가진 Section 들을 TransferLine 로 묶어 반환합니다.
 *
 * Path.findArrivalTime 메서드 내에서 TransferLine 를 생성 한다면 SRP 위반이라고 생각해
 * TransferLine 생성 과정을 추상화 하기 위해 작성 했습니다.
 * */
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
