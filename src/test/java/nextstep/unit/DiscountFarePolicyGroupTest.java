package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.farepolicy.DiscountFarePolicyGroup;
import nextstep.subway.domain.farepolicy.FarePolicyGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 할인 정책에 대한 테스트")
public class DiscountFarePolicyGroupTest {
  public static final int ZERO = 0;
  public static final int LINE_FARE = 1000;
  public static final int DEFAULT_FARE = 1250;
  public static final int UNDER_THIRTEEN = 8;
  public static final int AGE = 19;
  public static final int FIFTEEN = 15;
  public static final int TEEN_AGE = 15;

  @ParameterizedTest
  @CsvSource(value = {"0,450", "1,450", "2,450", "3,450", "4,450", "5,450", "6,450", "10,450"})
  @DisplayName("단거리 요금일 때, 유아요금일때의 요금을 계산합니다.")
  void calculateFareUnderTen(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //When
    DiscountFarePolicyGroup discountFarePolicyGroup = DiscountFarePolicyGroup.of();
    //Then
    int totalSum = discountFarePolicyGroup.calculateFare(DEFAULT_FARE + additionalFare, UNDER_THIRTEEN);
    assertThat(totalSum).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"12,500", "13,500", "16,550", "20,550", "21,600"})
  @DisplayName("중거리 요금일때 유아요금일때의 요금을 계산합니다.")
  void calculateFareOverTen(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //When
    DiscountFarePolicyGroup discountFarePolicyGroup = DiscountFarePolicyGroup.of();
    //Then
    int totalSum = discountFarePolicyGroup.calculateFare(DEFAULT_FARE + additionalFare, UNDER_THIRTEEN);
    assertThat(totalSum).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"50,2050","51,2150","59,2250","100,2750","150,3350"})
  @DisplayName("장거리 요금일때, 일반 요금을 계산합니다.")
  void calculateFareOverFifty(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //When
    DiscountFarePolicyGroup discountFarePolicyGroup = DiscountFarePolicyGroup.of();
    //Then
    int totalSum = discountFarePolicyGroup.calculateFare(DEFAULT_FARE + additionalFare, AGE);
    assertThat(totalSum).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"50,2160","51,2240","59,2320","100,2720","150,3200"})
  @DisplayName("장거리 요금일때, 노선 요금이 있을때, 청소년 요금 계산합니다.")
  void calculateFareOverFiftyWithLineFare(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    int additionalFare = farePolicyGroup.calculateFare(distance, LINE_FARE);
    //When
    DiscountFarePolicyGroup discountFarePolicyGroup = DiscountFarePolicyGroup.of();
    //Then
    int totalSum = discountFarePolicyGroup.calculateFare(DEFAULT_FARE + additionalFare, TEEN_AGE);
    assertThat(totalSum).isEqualTo(fare);
  }
}
