package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TimetablesResponseView {
    private List<String> up;
    private List<String> down;

    public TimetablesResponseView() {
    }

    @Builder
    private TimetablesResponseView(List<String> up, List<String> down) {
        this.up = up;
        this.down = down;
    }

    public static TimetablesResponseView of(final List<String> up, final List<String> down) {
        return TimetablesResponseView.builder()
                .up(up)
                .down(down).build();
    }
}
