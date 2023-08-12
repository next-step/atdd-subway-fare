package nextstep.subway.domain.farepolicy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.domain.farepolicy.type.PolicyType;

public class FarePolicyGroup {

  public static final int ZERO = 0;
  public static final int EXTRA_FARE = 100;
  public static final int PER_KILOMETER = 5;
  public static final int LOWER_THRESHOLD_OVER_TEN = 10;
  public static final int UPPER_THRESHOLD_OVER_FIFTY = 50;
  public static final int PER_EIGHT_KILOMETER = 8;
  private final List<AdditionalFarePolicy> farePolicyGroup = new ArrayList<>();

  private FarePolicyGroup() {
    AdditionalFarePolicy OverTenKilometerPolicy = new DistanceFarePolicy(UPPER_THRESHOLD_OVER_FIFTY,
        LOWER_THRESHOLD_OVER_TEN, EXTRA_FARE, PER_KILOMETER);
    AdditionalFarePolicy OverFiftyKilometerPolicy = new DistanceFarePolicy(Integer.MAX_VALUE,
        UPPER_THRESHOLD_OVER_FIFTY, EXTRA_FARE, PER_EIGHT_KILOMETER);
    AdditionalFarePolicy additionalLinePolicy = new LineFarePolicy();

    addPolicy(OverTenKilometerPolicy);
    addPolicy(OverFiftyKilometerPolicy);
    addPolicy(additionalLinePolicy);
  }

  //TODO
  // 여기에는 Policy가 생성되서 Fare에 주입될 수 있습니다.
  // 이번 요구사항은 static이지만 미래에 요구사항이 변경될 때,
  // 여기에 추가를 하거나 custom 값을 받아서 추가할 수 있게 할 수 있습니다.
  public static FarePolicyGroup of() {
    return new FarePolicyGroup();
  }

  private void addPolicy(AdditionalFarePolicy farePolicy) {
    farePolicyGroup.add(farePolicy);
  }

  public int calculateFare(int distance, int lineFare){
    int additional = ZERO;

    additional += farePolicyGroup.stream()
        .filter(farePolicy -> farePolicy.getPolicyType().equals(PolicyType.DISTANCE) && farePolicy.isSatisfied(distance))
        .map(farePolicy -> farePolicy.calculateFare(distance))
        .reduce(ZERO, Integer::sum);

    additional += farePolicyGroup.stream()
        .filter(farePolicy -> farePolicy.getPolicyType().equals(PolicyType.ADDITIONAL_LINE))
        .findFirst()
        .map(farePolicy -> farePolicy.calculateFare(lineFare))
        .orElse(ZERO);

    return additional;
  }

  // CUSTOM POLICY를 추가 할 수 있습니다.
  public void addCustomPolicy(int value, PolicyType policyType){
  }
  private void validateAgeFarePolicyGroup(){
    //throw new IllegalArgumentException("나이에 대한 할인 정책은 하나만 가능합니다.");
    //throw new IllegalArgumentException("나이에 대한 할인 정책은 최종 금액에만 가능합니다.");
  }
  private void validateFarePolicyGroup(){
    //throw new IllegalArgumentException("나이에 대한 할인 정책은 하나만 가능합니다.")
  }
}
