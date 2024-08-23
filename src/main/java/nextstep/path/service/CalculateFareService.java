package nextstep.path.service;

import nextstep.path.dto.*;
import org.springframework.stereotype.Service;

@Service
public class CalculateFareService {

    public Path calculateFare(Path path) {
        CalculateOverFare.of(path);
        CalculateAdditionalFare.of(path);
        CalculateMemberAgeFare.of(path);
        return path;
    }
}
