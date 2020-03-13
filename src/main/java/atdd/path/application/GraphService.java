package atdd.path.application;

import atdd.path.dao.LineDao;
import atdd.path.domain.Line;
import atdd.path.domain.MinTimePathGraph;
import atdd.path.domain.PathGraph;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {
    private LineDao lineDao;

    public GraphService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public List<Station> findPath(long startId, long endId) {
        List<Line> lines = lineDao.findAll();
        PathGraph graph = new PathGraph(lines);
        return graph.getShortestDistancePath(startId, endId);
    }

    public List<Station> findMinTimePath(long startId, long endId) {
        List<Line> lines = lineDao.findAll();

        MinTimePathGraph graph = new MinTimePathGraph(lines);
        List<Station> pathStation = graph.getShortestTimeDistancePath(startId, endId);

        return pathStation;
    }

    public List<Line> findAllLine(){
        return lineDao.findAll();
    }
}
