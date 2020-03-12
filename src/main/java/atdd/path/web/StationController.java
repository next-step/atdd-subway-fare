package atdd.path.web;

import atdd.path.application.dto.CreateStationRequestView;
import atdd.path.application.dto.StationResponseDto;
import atdd.path.domain.Station;
import atdd.path.repository.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("stations")
public class StationController {
    private StationRepository stationRepository;

    public StationController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @PostMapping
    public ResponseEntity createStation(@RequestBody CreateStationRequestView view) {
        Station persistStation = stationRepository.save(view.toStation());
        return ResponseEntity
                .created(URI.create("/stations/" + persistStation.getId()))
                .body(StationResponseDto.of(persistStation));
    }

    @GetMapping("{id}")
    public ResponseEntity retrieveStation(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(it -> ResponseEntity.ok().body(StationResponseDto.of(it)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity showStation() {
        List<Station> persistStations = stationRepository.findAll();
        return ResponseEntity.ok().body(StationResponseDto.listOf(persistStations));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{stationId}/timetables")
    public ResponseEntity retrieveStationTimetable(@PathVariable Long stationId) {
        return ResponseEntity.ok().build();
    }
}
