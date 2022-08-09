package reactor.study.demo;

import java.util.concurrent.Executors;

import org.junit.Test;

import lombok.AllArgsConstructor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author 陈添明
 * @since 2022/8/5 6:35 下午
 */
public class SchedulerTest extends AbstractReactorTest {

    @Test
    public void testImmediate() {
        Scheduler.Worker worker = Schedulers.immediate().createWorker();
        for (int i = 0; i < 10; i++) {
            worker.schedule(new Task(i, 1000));
        }
    }

    @Test
    public void testSingle() {
        Scheduler.Worker worker = Schedulers.newSingle("test").createWorker();
        for (int i = 0; i < 10; i++) {
            worker.schedule(new Task(i, 1000));
        }
        sleep(10000);
    }

    @Test
    public void testElastic() {
        Scheduler scheduler = Schedulers.elastic();
        for (int i = 0; i < 10; i++) {
            scheduler.schedule(new Task(i, 1000));
        }
        sleep(10000);
    }

    @Test
    public void testElasticWorker() {
        Scheduler.Worker worker = Schedulers.elastic().createWorker();
        for (int i = 0; i < 10; i++) {
            worker.schedule(new Task(i, 1000));
        }
        sleep(10000);
    }

    @Test
    public void testBoundElasticWorker() {
        Scheduler scheduler = Schedulers.newBoundedElastic(3, 10, "be");
        for (int i = 0; i < 10; i++) {
            scheduler.schedule(new Task(i, 1000));
        }
        sleep(10000);
    }

    @Test
    public void testFromExecutorService() {
        Scheduler.Worker worker = Schedulers.fromExecutorService(Executors.newFixedThreadPool(10)).createWorker();
        for (int i = 0; i < 10; i++) {
            worker.schedule(new Task(i, 1000));
        }
        sleep(10000);
    }



    @Test
    public void test() {
        delayPublishFlux(10, 1, 10)
                .publishOn(Schedulers.fromExecutorService(Executors.newFixedThreadPool(10)))
                .doOnNext(x -> sleep(1000))
                .subscribe(i -> logInt(i, "消费"));
        sleep(10000);
    }



    @AllArgsConstructor
    public class Task implements Runnable {

        private final int id;

        private final int runMillis;

        @Override
        public void run() {
            sleep(runMillis);
            logInt(id, "任务执行");
        }
    }
}
