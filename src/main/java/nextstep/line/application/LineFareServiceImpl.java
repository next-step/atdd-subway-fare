package nextstep.line.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineSections;
import nextstep.line.domain.Section;
import nextstep.line.persistance.LineRepository;
import nextstep.path.domain.LineFare;
import nextstep.path.service.LineFareService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineFareServiceImpl implements LineFareService {

    private final LineRepository lineRepository;

    public LineFareServiceImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }



}
