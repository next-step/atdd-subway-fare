package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StationResponseDto {
    private Long id;
    private String name;
    private Set<ItemView> lines;

    @Builder
    public StationResponseDto(Long id, String name, Set<ItemView> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public static StationResponseDto of(Station station) {
        return StationResponseDto.builder()
                .id(station.getId())
                .name(station.getName())
                .lines(station.getLines().stream()
                        .map(it -> ItemView.of(it.getId(), it.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    public static List<StationResponseDto> listOf(List<Station> station) {
        return station.stream()
                .map(it -> StationResponseDto.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .lines(it.getLines().stream()
                                .map(it2 -> ItemView.of(it2.getId(), it2.getName()))
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }

    public Station toStation() {
        return new Station(this.name);
    }
}
