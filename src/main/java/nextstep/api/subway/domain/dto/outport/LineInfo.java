package nextstep.api.subway.domain.dto.outport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Rene Choi
 * @since : 2024/02/28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LineInfo {
	private Long id;
	private String name;
	private String color;
}
