package karate;

import com.intuit.karate.junit5.Karate;

class KarateTest {

    @Karate.Test
    Karate runAll() {
        return Karate.run("order").relativeTo(getClass());
    }
}