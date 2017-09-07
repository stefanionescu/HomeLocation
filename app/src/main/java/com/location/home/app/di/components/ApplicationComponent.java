package com.location.home.app.di.components;

import android.content.Context;

import com.location.home.app.HomeLocationApplication;
import com.location.home.app.di.modules.ApplicationModule;
import com.location.home.app.di.modules.ExecutorModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ExecutorModule.class})
public interface ApplicationComponent {

    void inject(HomeLocationApplication baseActivity);

    Context context();

}
