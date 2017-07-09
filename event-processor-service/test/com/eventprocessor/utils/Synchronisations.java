package com.eventprocessor.utils;

import static org.junit.Assert.fail;

/**
 * @author Artem
 * @since 12.06.2017.
 */
public class Synchronisations {
    public static void waitFor(Condition condition) throws InterruptedException {
        int timeToSleep = 100;
        int times = 500;
        while (times-- > 0) {
            try {
                condition.test();
                return;
            } catch (AssertionError ignored) {
                Thread.sleep(timeToSleep);
            }
        }
        fail();
    }

    public interface Condition {
        void test();
    }
}
