package de.arthurpicht.taskRunner.integrationTest;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalUnit;

public class DateTimeTest {

    @Test
    void test() throws InterruptedException {

        LocalDateTime start = LocalDateTime.now();

        Thread.sleep(1000);

        LocalDateTime stop = LocalDateTime.now();

        Duration duration = Duration.between(start, stop);

        System.out.println(duration.toSeconds());
        System.out.println(duration.getSeconds());



    }

}
