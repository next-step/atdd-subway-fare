package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.springframework.stereotype.Service;

@Service
public class FareService {

	public Fare calculateAmount(Path path, Integer age) {

		FarePolicies farePolicies = new FarePolicies();
		farePolicies.addPolicy(new DistanceFarePolicy(path.extractDistance()));
		farePolicies.addPolicy(new LineFarePolicy(path.getExtraFareOnLine()));
		farePolicies.addPolicy(new AgeDiscountFarePolicy(age));

		return farePolicies.calculateAll();
	}
}
