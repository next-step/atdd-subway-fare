package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StationsResponseDto {
    private List<StationResponseDto> stations;

    @Builder
    public StationsResponseDto(List<StationResponseDto> stations) {
        this.stations = stations;
    }

    public static StationsResponseDto of(List<Station> station) {
        return StationsResponseDto.builder().stations(station.stream()
                .map(it -> StationResponseDto.of(it))
                .collect(Collectors.toList()))
                .build();
    }
}
