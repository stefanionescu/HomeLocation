package com.location.home.executor;

/**
 * @author stefan
 */
public interface MainThreadExecutor {

    void execute(Runnable runnable);
}
