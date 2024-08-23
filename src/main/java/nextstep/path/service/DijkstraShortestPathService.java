package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.domain.GraphModel;
import nextstep.path.dto.Path;

import java.util.List;

public class DijkstraShortestPathService implements PathService {
    @Override
    public Path findPath(final Member member, final String type, final Long source, final Long target, final List<Line> lineList) {
        GraphModel graphModel = new GraphModel(source, target);
        return graphModel.findPath(member, lineList, type);
    }
}

