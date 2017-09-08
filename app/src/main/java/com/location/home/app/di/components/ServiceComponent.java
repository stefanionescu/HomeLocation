package com.location.home.app.di.components;

import com.location.home.app.di.modules.LocateUseCaseModule;
import com.location.home.app.di.modules.ServiceModule;
import com.location.home.app.di.scopes.ServiceScope;
import com.location.home.device.GpsService;

import dagger.Component;

@ServiceScope
@Component(modules = {LocateUseCaseModule.class, ServiceModule.class})
public interface ServiceComponent {

    void inject(GpsService gps);

}
