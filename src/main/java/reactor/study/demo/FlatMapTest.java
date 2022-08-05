package reactor.study.demo;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/6/9 4:06 下午
 */
public class FlatMapTest extends AbstractReactorTest {

    public Flux<Integer> flat(int delayMillis, int i) {
        return delayPublishFlux(delayMillis, i * 10, i * 10 + 5);
    }

    public Flux<Integer> flatRange(int i) {
        return Flux.range(i * 10, 4);
    }

    @Test
    public void test() {
        delayPublishFlux(100, 1, 6)
                .doOnRequest(r -> logLong(r, "main-request"))
                .flatMap((i) -> flat(1000, i).doOnRequest(r -> logLong(r, "inner-request"))
                        .subscribeOn(Schedulers.newElastic("inner")), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testSync() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flat(1000, i), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testSubscribeOn() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flat(1000, i)
                        .subscribeOn(Schedulers.newElastic("inner")), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testPublishOn() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flat(10, i)
                        .publishOn(Schedulers.newElastic("inner"))
                        // publishOn()后面至少要有一个操作符才会异步。
                        // 故意让下游执行慢一点，演示出交错效果
                        .doOnNext(x -> sleep(1000)), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testSubscribeOnPublishOn() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flat(1000, i)
                        .subscribeOn(Schedulers.newElastic("inner-sub"))
                        .publishOn(Schedulers.newElastic("inner-pub"))
                        // publishOn()后面至少要有一个操作符才会异步。
                        // 故意让下游执行慢一点，演示出交错效果
                        .doOnNext(x -> sleep(1000)), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testNoFused() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flat(100, i)
                        .subscribeOn(Schedulers.newElastic("inner")), 3, 2)
                .subscribe(i -> {
                    sleep(1000);
                    logInt(i, "消费");
                });
        sleep(10000);
    }

    @Test
    public void testSyncFused() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flatRange(i), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testAsyncFused() {
        delayPublishFlux(100, 1, 6)
                .flatMap((i) -> flatRange(i).publishOn(Schedulers.newElastic("inner")), 3, 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

}
