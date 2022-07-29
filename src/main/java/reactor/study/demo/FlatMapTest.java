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

    @Test
    public void testFlatMapMapperSync() {
        delayPublishFlux("flat", "map")
                .flatMap(this::delayPublishCharacter)
                .subscribe(this::logChar);
        sleep(10000);
    }

    @Test
    public void testFlatMapMapperSubscribeOn() {
        delayPublishFlux("flat", "map")
                .flatMap(s -> delayPublishCharacter(s).subscribeOn(Schedulers.boundedElastic()))
                .subscribe(this::logChar);
        sleep(10000);
    }

    @Test
    public void testAsyncSource() {
        Flux.just("flat", "map")
                .delayElements(Duration.ofMillis(1000))
                .flatMap(this::delayPublishCharacter)
                .subscribe(this::logChar);
        sleep(10000);
    }

    private Flux<Character> delayPublishCharacter(String s) {
        return Flux.create(fluxSink -> {
            for (char c : s.toCharArray()) {
                fluxSink.next(c);
                sleep(1000);
            }
            fluxSink.complete();
        });
    }

}
