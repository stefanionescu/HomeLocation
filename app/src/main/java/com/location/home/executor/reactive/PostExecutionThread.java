package com.location.home.executor.reactive;

import io.reactivex.Scheduler;

public interface PostExecutionThread {
  Scheduler getScheduler();
}
