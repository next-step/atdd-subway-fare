package nextstep.path.domain;


import java.util.Objects;

public class LineFare {

    private final long lineId;
    private final int extraFare;

    public LineFare(long lineId, int extraFare) {
        this.lineId = lineId;
        this.extraFare = extraFare;
    }

    public boolean hasExtraFare() {
        return 0 < extraFare;
    }


    public int getExtraFare() {
        return extraFare;
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineFare lineFare = (LineFare) o;
        return lineId == lineFare.lineId && extraFare == lineFare.extraFare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, extraFare);
    }
}
