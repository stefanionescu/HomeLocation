package com.location.home.app.di.modules;

import android.content.Context;

import com.location.home.app.di.scopes.ServiceScope;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.executor.ThreadExecutor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ExecutorModule.class)
public class LocateUseCaseModule {

    private Context context;

    public LocateUseCaseModule(Context context){

        this.context = context;

    }

    @Provides
    @ServiceScope
    public LocateHome provideLocateHome(@Named("interactor_exec") ThreadExecutor threadExecutor) {

        return new LocateHome(threadExecutor, context);

    }

}
