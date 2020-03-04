package atdd.path.application;

import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TimeTableService {
    private LineDao lineDao;
    private StationDao stationDao;
    private EdgeDao edgeDao;

    public TimeTableService(LineDao lineDao, StationDao stationDao, EdgeDao edgeDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.edgeDao = edgeDao;
    }

    public LocalTime findFirstTimeForUp(Line line, Station station) {
        return null;
    }
}
