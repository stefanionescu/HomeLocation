package com.location.home.app.di.components;

import com.location.home.app.di.modules.MainPresenterModule;
import com.location.home.app.di.scopes.MainScope;
import com.location.home.ui.activities.MainActivity;

import dagger.Component;

@MainScope
@Component(dependencies = ApplicationComponent.class, modules = MainPresenterModule.class)
public interface MainComponent {

    void inject(MainActivity activity);

}
