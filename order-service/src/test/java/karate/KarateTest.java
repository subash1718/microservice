package karate;

import com.intuit.karate.junit5.Karate;
import com.microservice.order_service.OrderServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = OrderServiceApplication.class
)
@ActiveProfiles("test")
class KarateTest {

    @LocalServerPort
    int port;

    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:karate")
                .systemProperty("karate.port", String.valueOf(port));
    }
}
