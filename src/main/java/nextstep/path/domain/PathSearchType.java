package nextstep.path.domain;

import nextstep.line.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public enum PathSearchType {
    DURATION(section -> SectionEdge.of(section, section.getDuration())),
    DISTANCE(section -> SectionEdge.of(section, section.getDistance()));

    private final SectionConverter sectionConverter;

    PathSearchType(SectionConverter sectionConverter) {
        this.sectionConverter = sectionConverter;
    }

    public List<SectionEdge> mapToSectionEdges(List<Section> sections) {
        List<SectionEdge> edges = sections.stream()
                .map(sectionConverter::toEdge)
                .collect(Collectors.toList());

        List<SectionEdge> oppositeEdges = sections.stream()
                .map(Section::flip)
                .map(sectionConverter::toEdge)
                .collect(Collectors.toList());

        edges.addAll(oppositeEdges);

        return edges;
    }
}
