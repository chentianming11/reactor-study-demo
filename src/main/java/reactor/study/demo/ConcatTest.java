package reactor.study.demo;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;

/**
 * @author 陈添明
 * @since 2022/7/11 7:28 下午
 */
public class ConcatTest extends AbstractReactorTest {

    @Test
    public void test() throws InterruptedException {
        Flux<Integer> a = delayPublishFlux(1, 4);
        Flux<Integer> b = delayPublishFlux(4, 7);
        Flux.concat(a, b).subscribe(this::logInt);
        Thread.sleep(10000);
    }

    @Test
    public void testAsyncDelay() throws InterruptedException {
        Flux<Integer> a = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(1000));
        Flux<Integer> b = Flux.just(4, 5, 6).delayElements(Duration.ofMillis(1000));
        Flux.concat(a, b).subscribe(this::logInt);
        Thread.sleep(10000);
    }
}
