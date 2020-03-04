package atdd.path.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

public class TimeTableResponseResource extends EntityModel<TimeTableFinalResponse> {
    public TimeTableResponseResource(TimeTableFinalResponse content, Link... links) {
        super(content, links);
    }
}
