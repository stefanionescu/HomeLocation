package com.location.home.app.di.modules;

import com.location.home.executor.InteractorExecutor;
import com.location.home.executor.MainThreadExecutor;
import com.location.home.executor.MainThreadExecutorImp;
import com.location.home.executor.ThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ExecutorModule {

    @Provides
    @Named("interactor_exec")
    public InteractorExecutor provideExecutor() {
        return new ThreadExecutor();
    }

    @Provides
    @Named("main_exec")
    public MainThreadExecutor provideMainThreadExecutor() {
        return new MainThreadExecutorImp();
    }

}
