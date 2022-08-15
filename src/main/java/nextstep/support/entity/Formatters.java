package nextstep.support.entity;

import java.time.format.DateTimeFormatter;

public interface Formatters {
    DateTimeFormatter DATE_TIME_PATH = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
}
