package com.location.home.app.di.modules;

import com.location.home.domain.gethomelocation.FetchHome;
import com.location.home.executor.PostExecutionThread;
import com.location.home.executor.ThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ExecutorModule.class)
public class FetchUseCaseModule {

    @Provides
    public FetchHome provideFetchHome(@Named("interactor_exec") ThreadExecutor threadExecutor,
                                      @Named("main_exec") PostExecutionThread postExecutionThread){

        return new FetchHome(threadExecutor, postExecutionThread);

    }

}
