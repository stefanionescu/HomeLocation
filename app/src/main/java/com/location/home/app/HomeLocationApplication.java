package com.location.home.app;

import android.app.Application;

import com.location.home.app.di.components.ApplicationComponent;
import com.location.home.app.di.components.DaggerApplicationComponent;
import com.location.home.app.di.modules.ApplicationModule;
import com.location.home.app.di.modules.ExecutorModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class HomeLocationApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {

        super.onCreate();

        initializeInjector();
        setupLeakCanary();

    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .executorModule(new ExecutorModule())
                .build();

        applicationComponent.inject(this);

    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

}
