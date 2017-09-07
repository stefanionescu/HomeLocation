package com.location.home.app.di.components;

import com.location.home.app.di.scopes.ServiceScope;
import com.location.home.device.GpsService;

import javax.inject.Singleton;

import dagger.Component;

@ServiceScope
@Component(dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    void inject(GpsService gps);

}
