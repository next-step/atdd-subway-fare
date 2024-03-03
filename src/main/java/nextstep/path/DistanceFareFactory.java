package nextstep.path;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceFareFactory {

    public static List<DistanceFare> createDistanceFare(int distance) {
        return List.of(
                new BasicDistanceFare(distance),
                new SecondDistanceFare(distance),
                new ThirdDistanceFare(distance));
    }
}
