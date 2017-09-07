package com.location.home.app.di.modules;

import com.location.home.app.di.scopes.MainScope;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.executor.PostExecutionThread;
import com.location.home.executor.ThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ExecutorModule.class)
public class UseCasesModule {

    @Provides
    @MainScope
    public LocateHome provideLocateHome(@Named("interactor_exec") ThreadExecutor threadExecutor,
                                        @Named("main_exec") PostExecutionThread postExecutionThread) {

        return new LocateHome(threadExecutor, postExecutionThread);

    }

}
