package nextstep.path.domain;

import nextstep.line.domain.Section;

public enum PathSearchType {
    DURATION(section -> SectionEdge.of(section, section.getDuration())),
    DISTANCE(section -> SectionEdge.of(section, section.getDistance()));

    private final SectionConverter sectionConverter;

    PathSearchType(SectionConverter sectionConverter) {
        this.sectionConverter = sectionConverter;
    }

    public SectionEdge mapToEdge(Section section) {
        return sectionConverter.toEdge(section);
    }
}
