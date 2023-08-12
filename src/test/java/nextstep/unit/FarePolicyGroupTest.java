package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.farepolicy.FarePolicyGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 정책에 대한 테스트")
public class FarePolicyGroupTest {
  public static final int ZERO = 0;
  public static final int LINE_FARE = 1000;

  @ParameterizedTest
  @CsvSource(value = {"0", "1", "2", "3", "4", "5", "6", "10"})
  @DisplayName("단거리 요금일때 추가 요금 계산합니다.")
  void calculateFareUnderTen(int distance) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    //When
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //Then
    assertThat(additionalFare).isEqualTo(ZERO);
  }

  @ParameterizedTest
  @CsvSource(value = {"12,100", "13,100", "16,200", "20,200", "21,300"})
  @DisplayName("중간거리 요금일때 추가 요금 계산 합니다.")
  void calculateFareOverTen(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    //When
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //Then
    assertThat(additionalFare).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"50,800","51,900","59,1000","100,1500","150,2100"})
  @DisplayName("장거리 요금일때 추가 요금 계산합니다.")
  void calculateFareOverFifty(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    //When
    int additionalFare = farePolicyGroup.calculateFare(distance, FarePolicyGroup.ZERO);
    //Then
    assertThat(additionalFare).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"50,1800","51,1900","59,2000","100,2500","150,3100"})
  @DisplayName("장거리 요금일때, 최고 line 요금이 있을때, 요금 계산합니다.")
  void calculateFareOverFiftyWithLineFare(int distance, int fare) {
    FarePolicyGroup farePolicyGroup = FarePolicyGroup.of();
    //When
    int additionalFare = farePolicyGroup.calculateFare(distance, LINE_FARE);
    //Then
    assertThat(additionalFare).isEqualTo(fare);
  }
}
