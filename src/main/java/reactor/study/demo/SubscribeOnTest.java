package reactor.study.demo;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/7/29 5:49 下午
 */
public class SubscribeOnTest extends AbstractReactorTest {

    @Test
    public void testOnlySubscribeSwitch() {
        Flux.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(this::logInt);
        sleep(10000);
    }



}
