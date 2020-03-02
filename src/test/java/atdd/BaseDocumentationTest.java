package atdd;

import atdd.RestDocsConfig;
import atdd.path.domain.Station;
import atdd.user.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static atdd.TestConstant.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(RestDocsConfig.class)
@Ignore
public class BaseDocumentationTest {
    public static final LocalTime START_TIME = LocalTime.of(5, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 55);
    public static final int INTERVAL_MIN = 10;
    public static final int DISTANCE_KM = 5;
    public static final String NAME = "brown";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    public static Long stationId1=1L;
    public static Long stationId2=2L;
    public static Long stationId3=3L;
    public static Station STATION_1 = new Station(1L, STATION_NAME);
    public static Station STATION_2 = new Station(2L, STATION_NAME_2);
    public static Station STATION_3 = new Station(3L, STATION_NAME_3);
}
