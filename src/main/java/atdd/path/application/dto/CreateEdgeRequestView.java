package atdd.path.application.dto;

import lombok.Getter;

@Getter
public class CreateEdgeRequestView {
    private Long sourceId;
    private Long targetId;
    private int distance;
    private int elapsedTime;
}
