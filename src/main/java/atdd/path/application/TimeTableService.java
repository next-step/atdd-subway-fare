package atdd.path.application;

import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.domain.TimeTables;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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

    public LocalTime calculateFirstTime(Line line, int index){
        int intervalOfLine = line.getInterval();
        int minutesFromLineFirst = (intervalOfLine)*(index);
        LocalTime firstTime = line.getStartTime().plusMinutes(minutesFromLineFirst);
        return firstTime;
    }

    public LocalTime calculateLastTime(Line line, int index) {
        int intervalOfLine = line.getInterval();
        int minutesFromLineLast = (intervalOfLine)*index;
        LocalTime lastTime = line.getEndTime().plusMinutes(minutesFromLineLast);
        return lastTime;
    }

    public int calculateIndex(List<Station> stations, Station station){
        int indexOfStation = -1;
        for(Station tmp:stations){
            indexOfStation++;
            if(station.equals(tmp)){
                break;
            }
        }
        return indexOfStation;
    }

    public int calculateIndexReverse(List<Station> stations, Station station) {
        int indexOfStation = -1;
        Collections.reverse(stations);

        for(Station tmp:stations){
            indexOfStation++;
            if(station.equals(tmp)){
                break;
            }
        }
        return indexOfStation;
    }

    public List<LocalTime> makeTimeTable(LocalTime firstTime,
                                         LocalTime lastTime, int interval) {
        List<LocalTime> timeTable = new ArrayList<>();
        timeTable.add(firstTime);
        LocalTime nextTime=firstTime.plusMinutes(interval);
        while(nextTime.isBefore(lastTime)){
           timeTable.add(nextTime);
           nextTime=nextTime.plusMinutes(interval);
        }
        if(nextTime.equals(lastTime)){
            timeTable.add(nextTime);
        }
        return timeTable;
    }


    public TimeTables showTimeTables(Line line, List<Station> stations, Station station) {
        int index = calculateIndex(stations, station);
        int reverseIndex = calculateIndexReverse(stations, station);

        LocalTime firstTime = calculateFirstTime(line, index);
        LocalTime firstTimeReverse = calculateFirstTime(line, reverseIndex);
        LocalTime lastTime = calculateLastTime(line, index);
        LocalTime lastTimeReverse = calculateLastTime(line, reverseIndex);

        List<LocalTime> timeTable = makeTimeTable(firstTime, lastTime, line.getInterval());
        List<LocalTime> timeTableReverse = makeTimeTable(firstTimeReverse, lastTimeReverse, line.getInterval());

        TimeTables timeTables = new TimeTables(timeTable, timeTableReverse);
        return timeTables;
    }

}
