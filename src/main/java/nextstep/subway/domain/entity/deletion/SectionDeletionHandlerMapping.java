package nextstep.subway.domain.entity.deletion;

import nextstep.common.exception.DeletionValidationException;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.vo.Sections;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionDeletionHandlerMapping {

    private final List<SectionDeletionHandler> handlerList = List.of(
            new DeleteSectionAtLastHandler(),
            new DeleteSectionAtTopHandler(),
            new DeleteSectionAtMiddleHandler()
    );

    public SectionDeletionHandler getHandler(Sections sections, Station station) {
        for  (SectionDeletionHandler handler : handlerList) {
            if (handler.checkApplicable(sections, station)) {
                return handler;
            }
        }
        throw new DeletionValidationException("section.0004");
    }
}
