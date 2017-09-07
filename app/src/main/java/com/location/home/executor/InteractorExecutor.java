package com.location.home.executor;

/**
 * @author stefan
 */
public interface InteractorExecutor {

    void run(Interactor interactor);

    void destroyTasks();

    boolean isShutdown();
}
