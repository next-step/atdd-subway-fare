package nextstep.subway.domain;

public class DurationMinutes {
    private int sum = 0;
    private boolean isDone = false;

    public void processDownToUp(Section section, Station targetStation) {
        if (isDone) return;

        if (section.getDownStation().equals(targetStation)) {
            isDone = true;
            return;
        }

        sum += section.getDuration();
    }

    public void processUpToDown(Section section, Station targetStation) {
        if (isDone) return;

        if (section.getUpStation().equals(targetStation)) {
            isDone = true;
            return;
        }

        sum += section.getDuration();
    }

    public int getSum() {
        return sum;
    }
}
