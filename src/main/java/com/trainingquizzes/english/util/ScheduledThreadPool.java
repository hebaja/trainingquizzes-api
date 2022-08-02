package com.trainingquizzes.english.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledThreadPool {
	
	private ScheduledThreadPool() {}
	
	private static ScheduledExecutorService scheduledThreadPool;

    public static ScheduledExecutorService getScheduledThreadPool() {
        return scheduledThreadPool;
    }

    public static void create() {
        scheduledThreadPool = Executors.newScheduledThreadPool(3);
    }

}
