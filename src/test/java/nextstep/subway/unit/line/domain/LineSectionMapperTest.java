package nextstep.subway.unit.line.domain;

import static nextstep.Fixtures.강남_역삼_구간;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import nextstep.subway.line.application.LineSectionMapper;
import nextstep.subway.line.application.dto.LineSectionRequest;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineSectionMapperTest {
  @Mock private StationReader stationReader;
  @InjectMocks private LineSectionMapper lineSectionMapper;

  @DisplayName("구간 요청을 구간 도메인 엔티티로 변환한다.")
  @Test
  void map() {
    LineSection section = 강남_역삼_구간();
    Station upStation = section.getUpStation();
    Station downStation = section.getDownStation();
    given(stationReader.readById(upStation.getId())).willReturn(upStation);
    given(stationReader.readById(downStation.getId())).willReturn(downStation);

    LineSectionRequest request =
        new LineSectionRequest(
            upStation.getId(), downStation.getId(), section.getDistance(), section.getDuration());

    LineSection actualSection = lineSectionMapper.map(request);

    assertThat(actualSection.isSame(section)).isTrue();
  }
}
