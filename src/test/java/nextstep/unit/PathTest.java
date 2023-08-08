package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PathTest {
  @ParameterizedTest
  @CsvSource(value = {"0,1250", "1,1250", "2,1250", "3,1250", "4,1250", "5,1250", "6,1250", "10,1250", "12,1350", "13,1350", "16,1450", "20,1450", "21,1550"})
  @DisplayName("10km 이하까지 요금 계산합니다.")
  void calculateFareUnderTen(int distance, int fare) {
    Path path = new Path(new Sections());
    //When
    int resultFare = path.calculateFare(distance);
    //Then
    assertThat(resultFare).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"12,1350", "13,1350", "16,1450", "20,1450", "21,1550"})
  @DisplayName("10km 이상부터 50km 이하 일때 요금 계산 합니다.")
  void calculateFareOverTen(int distance, int fare) {
    Path path = new Path(new Sections());
    //When
    int resultFare = path.calculateFare(distance);
    //Then
    assertThat(resultFare).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource(value = {"50,2050","51,2150","59,2250","100,2750","150,3350"})
  @DisplayName("50 km 이상일때 요금 계산합니다.")
  void calculateFareOverFifty(int distance, int fare) {
    Path path = new Path(new Sections());
    //When
    int resultFare = path.calculateFare(distance);
    //Then
    assertThat(resultFare).isEqualTo(fare);
  }
}
