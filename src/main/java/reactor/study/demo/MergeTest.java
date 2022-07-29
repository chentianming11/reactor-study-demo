package reactor.study.demo;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/7/12 3:39 下午
 */
public class MergeTest extends AbstractReactorTest {

    @Test
    public void testSync() {
        Flux<Integer> a = delayPublishFlux(1, 4);
        Flux<Integer> b = delayPublishFlux(4, 7);
        Flux.merge(a, b).subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testSubscribeOn() {
        Flux<Integer> a = delayPublishFlux(1, 4).subscribeOn(Schedulers.boundedElastic());
        Flux<Integer> b = delayPublishFlux(4, 7).subscribeOn(Schedulers.boundedElastic());
        Flux.merge(a, b).subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testPublishOn() {
        // publishOn()后面至少要有一个操作符才会异步。
        Flux<Integer> a = delayPublishFlux(1, 4).publishOn(Schedulers.boundedElastic()).map(i -> {
            // 为了演示交错效果，这里故意使map()操作执行慢一些。因为真正的元素下发是同步的，只有到publishOn()下游才变成异步的。
            sleep(2000);
            return i;
        });
        Flux<Integer> b = delayPublishFlux(4, 7).publishOn(Schedulers.boundedElastic()).map(i -> {
            sleep(2000);
            return i;
        });
        Flux.merge(a, b).subscribe(this::logInt);
        sleep(10000);
    }


    @Test
    public void testFluxCreateAsync() {
        Flux<Integer> a = delayPublishFluxAsync(1, 4);
        Flux<Integer> b = delayPublishFluxAsync(4, 7);
        Flux.merge(a, b).subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testSyncFuse() throws InterruptedException {
        Flux<Integer> a = Flux.just(1, 2, 3);
        Flux<Integer> b = Flux.just(4, 5, 6);
        Flux.merge(a, b).subscribe(this::logInt);
        Thread.sleep(10000);
    }

    @Test
    public void testAsyncFuse() {
        Flux<Integer> a = Flux.just(1, 2, 3).publishOn(Schedulers.boundedElastic());
        Flux<Integer> b = Flux.just(4, 5, 6).publishOn(Schedulers.boundedElastic());
        Flux.merge(a, b).subscribe(this::logInt);
        sleep(10000);
    }

    @Test
    public void testAsyncDelay() throws InterruptedException {
        Flux<Integer> a = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(100));
        Flux<Integer> b = Flux.just(4, 5, 6).delayElements(Duration.ofMillis(100));
        Flux.merge(a, b).subscribe(this::logInt);
        Thread.sleep(3000);
    }

}
