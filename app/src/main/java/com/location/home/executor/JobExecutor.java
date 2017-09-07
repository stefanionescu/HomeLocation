package com.location.home.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

/**
 * Decorated {@link ThreadPoolExecutor}
 */

public class JobExecutor implements ThreadExecutor {
  private final ThreadPoolExecutor threadPoolExecutor;

    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();

  @Inject
  public JobExecutor() {
    this.threadPoolExecutor = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS,
        WORK_QUEUE, new JobThreadFactory());
  }

  @Override public void execute(@NonNull Runnable runnable) {
    this.threadPoolExecutor.execute(runnable);
  }

  private static class JobThreadFactory implements ThreadFactory {
    private int counter = 0;

    @Override public Thread newThread(@NonNull Runnable runnable) {
      return new Thread(runnable, "android_" + counter++);
    }
  }
}
