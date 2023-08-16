package nextstep.line.application;

import nextstep.exception.LineNotFoundException;
import nextstep.line.application.request.LineCreateRequest;
import nextstep.line.application.request.LineModifyRequest;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.application.response.LineResponse;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.line.domain.*;
import nextstep.line.domain.path.ShortPath;
import nextstep.line.domain.path.ShortPathType;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {

        Line line = new Line(lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                stationRepository.findStation(lineCreateRequest.getUpStationId()),
                stationRepository.findStation(lineCreateRequest.getDownStationId()),
                lineCreateRequest.getDistance(),
                lineCreateRequest.getDuration()
        );

        lineRepository.save(line);
        return LineResponse.of(line);
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new)
                .modify(lineModifyRequest.getName(), lineModifyRequest.getColor());
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(LineNotFoundException::new);
    }

    public ShortPathResponse findShortPath(ShortPathType type, Long startStationId, Long endStationId) {
        Station startStation = stationRepository.findStation(startStationId);
        Station endStation = stationRepository.findStation(endStationId);

        SubwayMap subwayMap = new SubwayMap(lineRepository.findAll());
        ShortPath shortPath = subwayMap.findShortPath(type, startStation, endStation);
        return ShortPathResponse.of(shortPath);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionAddRequest sectionAddRequest) {
        Station upStation = stationRepository.findStation(sectionAddRequest.getUpStationId());
        Station downStation = stationRepository.findStation(sectionAddRequest.getDownStationId());

        lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new)
                .addSection(upStation, downStation, sectionAddRequest.getDistance(), sectionAddRequest.getDuration());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Station station = stationRepository.findStation(stationId);

        lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new)
                .removeSection(station);
    }

}
