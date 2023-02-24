package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionCreateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse saveLine(LineCreateRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        if (request.getUpStationId() != null && request.getDownStationId() != null && request.getDistance() != 0) {
            Station upStation = stationService.findById(request.getUpStationId());
            Station downStation = stationService.findById(request.getDownStationId());
            line.addSection(upStation, downStation, request.getDistance(), request.getDuration());
        }
        return LineResponse.of(line);
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findById(id));
    }

    public Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void updateLine(Long id, LineCreateRequest lineCreateRequest) {
        Line line = findById(id);
        line.update(lineCreateRequest.getName(), lineCreateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        Station upStation = stationService.findById(sectionCreateRequest.getUpStationId());
        Station downStation = stationService.findById(sectionCreateRequest.getDownStationId());
        Line line = findById(lineId);

        line.addSection(upStation, downStation, sectionCreateRequest.getDistance(), sectionCreateRequest.getDuration());
    }

    private List<StationResponse> createStationResponses(Line line) {
        return line.getStations().stream()
                .map(it -> stationService.createStationResponse(it))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findById(lineId);
        Station station = stationService.findById(stationId);

        line.deleteSection(station);
    }
}
