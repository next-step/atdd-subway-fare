package atdd.path.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class TimeTableResponseResource extends EntityModel<TimeTableResponseView> {
    public TimeTableResponseResource(TimeTableResponseView content, Link... links) {
        super(content, links);
    }
}
