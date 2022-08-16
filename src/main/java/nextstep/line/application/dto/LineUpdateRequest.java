package nextstep.line.application.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Min(0)
    private int extraFare;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
