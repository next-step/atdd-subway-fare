package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

  @DisplayName("경로의 총 길이가 10Km이내면 요금은 1250원")
  @Test
  void calculate(){
    //given
    Fare fare = new Fare(10);
    //when
    long cost = fare.getCost();
    //then
    assertThat(cost).isEqualTo(1250);
  }

  @DisplayName("경로의 총 길이가 15km 요금은 1350원")
  @Test
  void calculateOver10(){
    //given
    Fare fare = new Fare(15);
    //when
    long cost = fare.getCost();
    //then
    assertThat(cost).isEqualTo(1350);
  }

  @DisplayName("경로의 총 길이가 57km 요금은 2050원")
  @Test
  void calculateOver50NotAdditionalFare(){
    //given
    Fare fare = new Fare(58);
    //when
    long cost = fare.getCost();
    //then
    assertThat(cost).isEqualTo(2150);
  }

  @DisplayName("경로의 총 길이가 58km 요금은 2150원")
  @Test
  void calculateOver50(){
    //given
    Fare fare = new Fare(58);
    //when
    long cost = fare.getCost();
    //then
    assertThat(cost).isEqualTo(2150);
  }

}