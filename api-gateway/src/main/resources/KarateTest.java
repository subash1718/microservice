package karate;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KarateTest {

    @LocalServerPort
    int port;

    @Karate.Test
    Karate testAll() {

        System.setProperty("karate.server.port", String.valueOf(port));

        return Karate.run("classpath:karate")
                .systemProperty("port", String.valueOf(port))
                .relativeTo(getClass());
    }
}
