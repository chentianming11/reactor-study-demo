package reactor.study.demo;

import java.util.function.Function;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/7/26 7:18 下午
 */
public class PublishOnTest extends AbstractReactorTest {

    @Test
    public void testNoFuse() {
        delayPublishFlux(1, 10)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testSyncFuse() {
        Flux.just(1, 2, 3, 4, 5)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testAsyncFuse() {
        Flux.just(1, 2, 3, 4, 5)
                .windowUntil(i -> i % 3 == 0)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(Function.identity())
                .subscribe(this::logInt);
        sleep(10000);
    }
}
