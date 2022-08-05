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
     * 每隔delayMillis生产一个元素
     */
    protected Flux<Integer> delayPublishFlux(int delayMillis, int startInclusive, int endExclusive) {
        return Flux.create(fluxSink -> {
            IntStream.range(startInclusive, endExclusive)
                    .forEach(i -> {
                        // 同步next
                        sleep(delayMillis);
                        logInt(i, "生产");
                        fluxSink.next(i);
                    });
            fluxSink.complete();
        });
    }

    /**
     * 每隔delayMillis生产一个元素，最后发送Error
     */
    protected Flux<Integer> delayPublishFluxError(int delayMillis, int startInclusive, int endExclusive) {
        return Flux.create(fluxSink -> {
            IntStream.range(startInclusive, endExclusive)
                    .forEach(i -> {
                        // 同步next
                        sleep(delayMillis);
                        logInt(i, "生产");
                        fluxSink.next(i);
                    });
            fluxSink.error(new RuntimeException("发布错误！"));
        });
    }


    protected Flux<String> delayPublishFlux(String... strings) {
        return Flux.create(fluxSink -> {
            for (String string : strings) {
                fluxSink.next(string);
                sleep(1000);
            }
            fluxSink.complete();
        });
    }

    protected Flux<Character> delayPublishCharacter(String s) {
        return Flux.create(fluxSink -> {
            for (char c : s.toCharArray()) {
                fluxSink.next(c);
                sleep(1000);
            }
            fluxSink.complete();
        });
    }


    @SneakyThrows
    protected void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        Thread.sleep(millis);
    }

    protected void logInt(int i, String action) {
        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time + " " + Thread.currentThread().getName() + " " + action + " " + i);
    }

    protected void logLong(long i, String action) {
        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time + " " + Thread.currentThread().getName() + " " + action + " " + i);
    }

    protected void logChar(char c) {
        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time + " " + Thread.currentThread().getName() + " " + c);
    }
}
