package com.location.home.app.di.modules;


import com.location.home.app.UIThread;
import com.location.home.executor.JobExecutor;
import com.location.home.executor.PostExecutionThread;
import com.location.home.executor.ThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ExecutorModule {

    @Provides
    @Named("interactor_exec")
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Named("main_exec")
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

}
