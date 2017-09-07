package com.location.home.app.di.modules;

import com.location.home.app.di.scopes.MainScope;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.domain.calculatehomelocation.LocateHomeImpl;
import com.location.home.executor.InteractorExecutor;
import com.location.home.executor.MainThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ExecutorModule.class)
public class UseCasesModule {

    @Provides
    @MainScope
    public LocateHome provideLocateHome(@Named("interactor_exec") InteractorExecutor interactorExecutor,
                                        @Named("main_exec") MainThreadExecutor mainThreadExecutor){

        return new LocateHomeImpl(interactorExecutor, mainThreadExecutor);

    }

}
