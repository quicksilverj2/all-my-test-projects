package com.edel.messagebroker.Tasks;

import com.msf.log.Logger;

import java.util.concurrent.Callable;

public abstract class BaseTask implements Callable<String> {

    private static Logger log = Logger.getLogger(BaseTask.class);

    protected String logClassName = this.getClass().getSimpleName();

    public abstract String process();

    public void startProcess(){
        String threadName = Thread.currentThread().getName();
        String firstPart = threadName.split(":")[0];
        Thread.currentThread().setName(firstPart+":"+logClassName);
        log.debug(logClassName+ " task started");
    }
}
