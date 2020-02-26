package atdd.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.annotation.DirtiesContext.ClassMode;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Import(RestDocsConfig.class)
public class BaseDocumentationTest {
    public static final String NAME = "brown";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String EMAIL_2 = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    public static final int INTERVAL_MIN = 10;
    public static final int DISTANCE_KM = 5;
    public static final LocalTime START_TIME = LocalTime.of(5, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 55);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;
}
