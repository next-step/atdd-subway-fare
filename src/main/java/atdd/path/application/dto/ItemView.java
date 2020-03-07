package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class ItemView {
    private Long id;
    private String name;

    @Builder
    public ItemView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ItemView of(Long id, String name) {
        return ItemView.builder().id(id).name(name).build();
    }

    public static List<ItemView> listOf(List<Station> paths) {
        return paths.stream()
                .map(it -> ItemView.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
