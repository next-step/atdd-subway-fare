package nextstep.subway.domain.chain;

import nextstep.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class FareHandlerFactory {
    private final List<FareHandler> fareHandlers;

    @Autowired
    public FareHandlerFactory(List<FareHandler> fareHandlers) {
        this.fareHandlers = fareHandlers;
        initializeChain(fareHandlers);
    }

    private void initializeChain(List<FareHandler> fareHandlers) {
        for (int i = 0; i < fareHandlers.size() - 1; i++) {
            fareHandlers.get(i).setNextHandler(fareHandlers.get(i + 1));
        }
    }

    public long calculateFare(long distance) {
        if (Objects.isNull(fareHandlers.get(0))) {
            throw new ApplicationException("FareHandler가 존재하지 않습니다.");
        }
        return fareHandlers.get(0).calculate(distance);
    }
}
