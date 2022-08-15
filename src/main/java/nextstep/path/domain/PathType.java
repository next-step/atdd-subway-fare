package nextstep.path.domain;

import nextstep.line.domain.Section;

public enum PathType {
    DURATION(section -> SectionEdge.of(section, section.getDuration())),
    DISTANCE(section -> SectionEdge.of(section, section.getDistance()));

    private final SectionConverter sectionConverter;

    PathType(SectionConverter sectionConverter) {
        this.sectionConverter = sectionConverter;
    }

    public SectionEdge mapToEdge(Section section) {
        return sectionConverter.toEdge(section);
    }
}
