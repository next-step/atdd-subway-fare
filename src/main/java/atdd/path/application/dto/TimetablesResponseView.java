package atdd.path.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TimetablesResponseView {
    private List<String> up;
    private List<String> down;

    public TimetablesResponseView() {
    }
}
