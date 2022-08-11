package nextstep.path.domain;

import nextstep.line.domain.Section;

@FunctionalInterface
public interface SectionConverter {
    SectionEdge toEdge(Section section);
}
