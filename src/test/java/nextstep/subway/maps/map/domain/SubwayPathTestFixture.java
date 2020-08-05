package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.utils.TestObjectUtils;

public class SubwayPathTestFixture {

    protected final Line line1;
    protected final LineStation lineStation1;
    protected final LineStation lineStation2;
    protected final Line line2;
    protected final LineStation lineStation3;
    protected final LineStation lineStation4;
    protected final Line line3;
    protected final LineStation lineStation5;
    protected final LineStation lineStation6;
    protected final LineStation lineStation7;

    public SubwayPathTestFixture() {
        line1 = TestObjectUtils.createLine(1L, "2호선", "GREEN");
        lineStation1 = new LineStation(1L, null, 0, 0);
        line1.addLineStation(lineStation1);
        lineStation2 = new LineStation(2L, 1L, 2, 2);
        line1.addLineStation(lineStation2);

        line2 = TestObjectUtils.createLine(2L, "신분당선", "RED");
        lineStation3 = new LineStation(2L, null, 0, 0);
        line2.addLineStation(lineStation3);
        lineStation4 = new LineStation(3L, 2L, 2, 1);
        line2.addLineStation(lineStation4);

        line3 = TestObjectUtils.createLine(3L, "3호선", "ORANGE");
        lineStation5 = new LineStation(1L, null, 0, 0);
        line3.addLineStation(lineStation5);
        lineStation6 = new LineStation(4L, 1L, 1, 2);
        line3.addLineStation(lineStation6);
        lineStation7 = new LineStation(3L, 4L, 2, 2);
        line3.addLineStation(lineStation7);
    }
}
