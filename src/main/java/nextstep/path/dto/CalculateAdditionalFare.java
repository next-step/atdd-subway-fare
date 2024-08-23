package nextstep.path.dto;

import nextstep.line.entity.Line;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;

import java.util.List;

public class CalculateAdditionalFare {

    private final static Long DEFAULT_ADDITIONAL_FARE = 0L;

    public static Path of(Path path) {
        Long totalPrice = calculateLineAdditionalFare(path.getLines(), path.getSections(), path.getTotalPrice());
        path.setTotalPrice(totalPrice);
        return path;
    }

    public static Long calculateLineAdditionalFare(final List<Line> lines, final Sections sections, final Long totalPrice) {
        long additionalFare = sections.getSections().stream()
                .mapToLong(section -> getMaxAdditionalFareForSection(lines, section))
                .max()
                .orElse(DEFAULT_ADDITIONAL_FARE);

        return totalPrice + additionalFare;
    }

    private static long getMaxAdditionalFareForSection(List<Line> lines, Section section) {
        return lines.stream()
                .filter(line -> line.getAdditionalFare() > DEFAULT_ADDITIONAL_FARE && line.hasSection(section))
                .mapToLong(Line::getAdditionalFare)
                .max()
                .orElse(DEFAULT_ADDITIONAL_FARE);
    }
}

