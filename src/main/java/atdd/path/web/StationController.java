package atdd.path.web;

import atdd.path.application.GraphService;
import atdd.path.application.TimeTableService;
import atdd.path.application.dto.*;
import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.domain.TimeTables;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static atdd.Constant.STATION_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(STATION_BASE_URI)
public class StationController {
    private StationDao stationDao;
    private LineDao lineDao;
    private EdgeDao edgeDao;
    private GraphService graphService;
    private TimeTableService timeTableService;

    public StationController(StationDao stationDao, LineDao lineDao, EdgeDao edgeDao,
                             GraphService graphService, TimeTableService timeTableService) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
        this.graphService = graphService;
        this.timeTableService = timeTableService;
    }

    @PostMapping
    public ResponseEntity createStation(@RequestBody CreateStationRequestView view) {
        Station persistStation = stationDao.save(view.toStation());
        return ResponseEntity
                .created(URI.create("/stations/" + persistStation.getId()))
                .body(StationResponseView.of(persistStation));
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieveStation(@PathVariable Long id) {
        try {
            Station persistStation = stationDao.findById(id);
            return ResponseEntity.ok().body(StationResponseView.of(persistStation));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity showStation() {
        List<Station> persistStations = stationDao.findAll();
        return ResponseEntity.ok().body(StationResponseView.listOf(persistStations));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping ("/{id}/timetables")
    public ResponseEntity retrieveTimetables(@PathVariable Long id){
        Station station = stationDao.findById(id);
        List<Line> lines = station.getLines();
        List<TimeTableResponseView> timeTablesForUpDown = new ArrayList<>();
        TimeTableResponseResource resource;
        TimeTableResponseView responseView;
        TimeTables timeTable = new TimeTables();
        for(Line line:lines){
            line = lineDao.findById(line.getId());
            TimeTables tmp
                    = timeTableService.showTimeTablesForUpDown(line, line.getStations(), station);
            responseView = new TimeTableResponseView(line.getId(), line.getName(), tmp);

            timeTablesForUpDown.add(responseView);
        }

        TimeTableFinalResponse timeTableFinalResponse
                = new TimeTableFinalResponse(timeTablesForUpDown);
        resource = new TimeTableResponseResource(timeTableFinalResponse);
        resource.add(
                linkTo(StationController.class)
                        .slash(id)
                        .slash("/timetables")
                        .withSelfRel());

        return ResponseEntity
                .created(URI.create("/stations/"+id+"/timetables"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}





