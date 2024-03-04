package nextstep.path;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceFareFactory {

    public static List<DistanceFare> createDistanceFare() {
        return List.of(
                new BasicDistanceFare(),
                new SecondDistanceFare(),
                new ThirdDistanceFare());
    }
}
