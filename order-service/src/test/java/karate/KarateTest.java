package karate;

import com.intuit.karate.junit5.Karate;
import com.microservice.order_service.OrderServiceApplication; // 🔥 IMPORTANT
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
        classes = OrderServiceApplication.class,   // 🔥 FIX
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class KarateTest {

    @LocalServerPort
    int port;

    @Karate.Test
    Karate testAll() {
        System.setProperty("karate.server.port", String.valueOf(port));
        return Karate.run("classpath:karate").relativeTo(getClass());
    }
}
