package reactor.study.demo;

import java.util.Date;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.DateFormatUtils;

import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

/**
 * @author 陈添明
 * @since 2022/7/26 2:28 下午
 */
public class AbstractReactorTest {

    /**
     * 每1s下发1个数
     */
    protected Flux<Integer> delayPublishFlux(int startInclusive, int endExclusive) {
        return Flux.create(fluxSink -> {
            IntStream.range(startInclusive, endExclusive)
                    .forEach(i -> {
                        // 同步next
                        fluxSink.next(i);
                        sleep(1000);
                    });
            fluxSink.complete();
        });
    }

    /**
     * 每一秒下发一个String
     */
    protected Flux<String> delayPublishFlux(String... strings) {
        return Flux.create(fluxSink -> {
            for (String string : strings) {
                fluxSink.next(string);
                sleep(1000);
            }
            fluxSink.complete();
        });
    }

    protected Flux<Integer> delayPublishFluxAsync(int startInclusive, int endExclusive) {
        return Flux.create(fluxSink -> {
            IntStream.range(startInclusive, endExclusive)
                    .forEach(i -> {
                        // 异步next
                        new Thread(() -> {
//                            System.out.println("fluxSink thread: " + Thread.currentThread().getName() + " " + i);
                            fluxSink.next(i);
                        }).start();
                    });
        });
    }

    @SneakyThrows
    protected void sleep(long millis) {
        Thread.sleep(millis);
    }

    protected void logInt(int i) {
        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time + " " + Thread.currentThread().getName() + " " + i);
    }

    protected void logChar(char c) {
        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time + " " + Thread.currentThread().getName() + " " + c);
    }
}
