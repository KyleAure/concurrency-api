package ee.jakarta.tck.concurrent.framework;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

public class AwaitilityTest {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    
    @Test
    public void testDoesTimeout() {
        System.out.println("------ TEST testDoesTimeout ------");
        System.out.println(">> START: " + LocalDateTime.now().format(formatter));
        try {
            Duration duration = Duration.ofMillis(400);
            Duration interval = Duration.ofMillis(200);
            Awaitility
                .await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(interval)
                .pollDelay(Duration.ofSeconds(0)) // start checking immediately
                .ignoreExceptionsInstanceOf(InterruptedException.class)
                .untilAsserted(() -> waitUntilCompleted(duration, true));
            return;
        } catch (Exception e) {
            System.out.println("DOCKER_HOST is not listening");
            return;
        } finally {
            System.out.println("<< FINISH: " + LocalDateTime.now().format(formatter));
        }
    }
    
    @Test
    public void testDoesNotTimeout() {
        System.out.println("------ TEST testDoesNotTimeout ------");
        System.out.println(">> START: " + LocalDateTime.now().format(formatter));
        try {
            Duration duration = Duration.ofMillis(400);
            Duration interval = Duration.ofMillis(200);
            Awaitility
                .await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(interval)
                .pollDelay(Duration.ofSeconds(0)) // start checking immediately
                .ignoreExceptionsInstanceOf(InterruptedException.class)
                .untilAsserted(() -> waitUntilCompleted(duration, false));
            return;
        } catch (Exception e) {
            System.out.println("DOCKER_HOST is not listening");
            return;
        } finally {
            System.out.println("<< FINISH: " + LocalDateTime.now().format(formatter));
        }
    }
    
    @Test
    public void testDoesHang() {
        System.out.println("------ TEST testDoesHang ------");
        System.out.println(">> START: " + LocalDateTime.now().format(formatter));
        try {
            Duration duration = Duration.ofMillis(400);
            Duration interval = Duration.ofMillis(200);
            Awaitility
                .await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(interval)
                .pollDelay(Duration.ofSeconds(0)) // start checking immediately
                .ignoreExceptionsInstanceOf(TimeoutException.class)
                .untilAsserted(() -> waitUntilCompleted(duration, null));
            return;
        } catch (Exception e) {
            System.out.println("DOCKER_HOST is not listening");
            return;
        } finally {
            System.out.println("<< FINISH: " + LocalDateTime.now().format(formatter));
        }
    }
    
    private static boolean waitUntilCompleted(Duration timeout, Boolean doesTimeout) throws InterruptedException, TimeoutException {
        //socket.connect(socketAddress, (int) timeout.toMillis())
        System.out.println("Timeout set to: " + timeout);
        System.out.println("Will timeout: " + doesTimeout);
        System.out.println("Will hang: " + (doesTimeout == null) );
        
        try {
            if(doesTimeout == null) {
                Thread.sleep(Duration.ofMinutes(1).toMillis()); //exceeds atMost 10 seconds
            }
            
            Thread.sleep(timeout.toMillis());
            
            if(doesTimeout) {
                throw new TimeoutException("Replicate a timeout exception");
            }    
            
            return true;
        } finally {
            System.out.println("Exit waitUntilCompleted");
        }
    }
    
    private static class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }
}
