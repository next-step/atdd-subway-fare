package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.Path;

import java.util.List;

public interface PathService {
    Path findPath(Member member, String type, Long source, Long target, List<Line> lineList);
}

