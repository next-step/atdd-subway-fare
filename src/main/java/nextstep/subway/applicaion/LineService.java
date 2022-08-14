package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineSectionRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineSectionRequest lineSectionRequest) {
        LineRequest lineRequest = lineSectionRequest.getLineRequest();
        SectionRequest sectionRequest = lineSectionRequest.getSectionRequest();

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), Fare.from(lineRequest.getFare())));
        if (validateSectionRequest(sectionRequest)) {
            Station upStation = stationService.findById(sectionRequest.getUpStationId());
            Station downStation = stationService.findById(sectionRequest.getDownStationId());
            line.addSection(new Line.SectionBuilder()
                    .upStation(upStation)
                    .downStation(downStation)
                    .distance(Distance.from(sectionRequest.getDistance()))
                    .duration(Duration.from(sectionRequest.getDuration()))
                    .build());
        }
        return LineResponse.of(line);
    }

    private boolean validateSectionRequest(SectionRequest sectionRequest) {
        return sectionRequest != null && sectionRequest.getUpStationId() != null && sectionRequest.getDownStationId() != null && sectionRequest.getDistance() != 0;
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
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = findById(lineId);

        line.addSection(new Line.SectionBuilder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(Distance.from(sectionRequest.getDistance()))
                .duration(Duration.from(sectionRequest.getDuration()))
                .build());
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
