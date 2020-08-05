package nextstep.subway.utils;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * HHmmss
 */
public class SimpleLocalTimeConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new IllegalArgumentException(
                    "The argument should be a string: " + source);
        }

        try {
            String dateString = (String) source;

            DateTimeFormatter formatter = dateString.length() > 4 ? DateTimeFormatter.ofPattern("HHmmss") : DateTimeFormatter.ofPattern("HHmm");

            return LocalTime.parse(dateString, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert", e);
        }
    }
}
