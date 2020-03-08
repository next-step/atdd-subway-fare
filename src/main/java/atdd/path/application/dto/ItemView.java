package atdd.path.application.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        return ItemView.builder()
                .id(id)
                .name(name)
                .build();
    }
}
