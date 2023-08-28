package nextstep.subway.domain.entity.deletion;

import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.vo.Sections;

public class DeleteSectionAtLastHandler extends SectionDeletionHandler{
    @Override
    public boolean checkApplicable(Sections sections, Station station) {
        return sections.getLastStation().equals(station);
    }

    @Override
    public void apply(Sections sections, Station station) {
        sections.forceSectionRemove(sections.getSectionByDownStation(station));
    }
}
