package reactor.study.demo;

import org.junit.Test;

import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/7/29 5:49 下午
 */
public class SubscribeOnTest extends AbstractReactorTest {


    @Test
    public void testBlock() {
        delayPublishFlux(1000, 1, 10)
                .doOnRequest(i -> logLong(i, "request"))
                .subscribeOn(Schedulers.newElastic("subscribeOn"))
                .publishOn(Schedulers.newElastic("publishOn"), 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }

    @Test
    public void testNoBlock() {
        delayPublishFlux(1000, 1, 10)
                .doOnRequest(i -> logLong(i, "request"))
                .subscribeOn(Schedulers.newElastic("subscribeOn"), false)
                .publishOn(Schedulers.newElastic("publishOn"), 2)
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }
}
