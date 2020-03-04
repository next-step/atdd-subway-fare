package atdd.path.application;

import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

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

//    public LocalTime findFirstTimeForUp(Line line, Station station) {
//        List<Station> stationsInLine = line.getStations();
//        int index = calculateIndex(stationsInLine, station);
//        LocalTime firstTimeOdStation = calculateFirstTime(line, index);
//        return firstTimeOdStation;
//    }

    private LocalTime calculateFirstTime(Line line, int indexOfStation){
        int intervalOfLine = line.getInterval();
        int minutesFromLineFirst = (intervalOfLine)*(indexOfStation);
        LocalTime firstTime = line.getStartTime().plusMinutes(minutesFromLineFirst);
        return firstTime;
    }

    public int calculateIndex(List<Station> stations, Station station){
        int indexOfStation = 0;
        for(Station tmp:stations){
            indexOfStation++;
            if(station.equals(tmp)){
                break;
            }
        }
        return indexOfStation;
    }
}
