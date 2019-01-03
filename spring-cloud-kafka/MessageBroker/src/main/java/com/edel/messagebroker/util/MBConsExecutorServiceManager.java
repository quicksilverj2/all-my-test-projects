package com.edel.messagebroker.util;

import com.edel.messagebroker.consumer.MBConsumerConfig;
import com.msf.log.Logger;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MBConsExecutorServiceManager {

    private static Logger log = Logger.getLogger(MBConsExecutorServiceManager.class);
    private static MBConsExecutorServiceManager instance;
    private static final Object lock = new Object();

    private ThreadPoolExecutor asyncExecutorService;

    public static MBConsExecutorServiceManager getInstance() {

        MBConsExecutorServiceManager r = instance;
        if (r == null) {
            synchronized (lock) {    // While we were waiting for the lock, another
                r = instance;        // thread may have instantiated the object.
                if (r == null) {
                    log.debug("Executor Service Manager Initialized");
                    instance = r = new MBConsExecutorServiceManager();
                }
            }
        }
        return r;
    }

    public static ExecutorService getAsyncExecutorService() {
        return getInstance().asyncExecutorService;
    }

    private MBConsExecutorServiceManager(){
        BasicThreadFactory asyncFactory = new BasicThreadFactory.Builder()
                .namingPattern("AES-%d:")
                .build();

        asyncExecutorService = new ThreadPoolExecutor(MBConsumerConfig.getThreadPoolCount(),
                2*MBConsumerConfig.getThreadPoolCount(),
                60000L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), asyncFactory);
    }

    // returns the largest number of threads that have ever been in the pool.
    public int getAsyncLargestPoolSizeAsync(){
        int largestPoolSize = ( asyncExecutorService).getLargestPoolSize();
        log.debug("Async Largest Pool Size " + largestPoolSize);
        return largestPoolSize;
    }

    public void shutDownAsnycESAndAwaitTermination(){
        log.debug("Async largest pool size " + getAsyncLargestPoolSizeAsync());
        asyncExecutorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!asyncExecutorService.awaitTermination(MBConsumerConfig.getESTimeOut(), TimeUnit.MILLISECONDS)) {
                asyncExecutorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!asyncExecutorService.awaitTermination(MBConsumerConfig.getESTimeOut(), TimeUnit.MILLISECONDS)) {
                    log.error("Async Executor Service did not terminate");
                }else{
                    log.error("Async Executor Service Shut Down afer Force Shutdown");
                }
            }else{
                log.debug("Async Executor Service Shut Down With Out Force ShutDown");
            }
        } catch (Exception ie) {
            log.error("Error ",ie);
            // (Re-)Cancel if current thread also interrupted
            asyncExecutorService.shutdownNow();
            // Preserve interrupt status
            //Thread.currentThread().interrupt();
        }
    }

}
