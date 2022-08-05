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
    public void testPreFetch() {
        delayPublishFlux(1000, 1, 5)
                .doOnRequest(i -> logLong(i, "request"))
                .publishOn(Schedulers.boundedElastic(), 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testDelayError() {
        delayPublishFluxError(500, 1, 5)
                .publishOn(Schedulers.boundedElastic())
                // 只是为了消费慢一点
                .doOnNext(i -> sleep(1000))
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testNotDelayError() {
        delayPublishFluxError(500, 1, 5)
                .publishOn(Schedulers.boundedElastic(), false, 256)
                // 只是为了消费慢一点
                .doOnNext(i -> sleep(1000))
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }


    @Test
    public void testNoFuse() {
        delayPublishFlux(1000, 1, 5)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testSyncFuse() {
        Flux.just(1, 2, 3, 4, 5)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testAsyncFuse() {
        Flux.just(1, 2, 3, 4, 5)
                .windowUntil(i -> i % 3 == 0)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(Function.identity())
                .subscribe(i1 -> logInt(i1, "消费"));
        sleep(10000);
    }
}
