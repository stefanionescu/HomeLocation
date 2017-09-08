package com.location.home.app.di.modules;

import android.content.Context;

import com.location.home.app.di.scopes.MainScope;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.domain.gethomelocation.FetchHome;
import com.location.home.ui.presenters.MainPresenter;
import com.location.home.ui.presenters.MainPresenterImp;
import com.location.home.ui.views.MainView;

import dagger.Module;
import dagger.Provides;

@Module(includes = FetchUseCaseModule.class)
public class MainPresenterModule {

    private MainView view;
    private Context context;

    public MainPresenterModule(MainView view, Context context) {

        this.view = view;
        this.context = context;

    }

    @Provides
    @MainScope
    public MainPresenter providePresenter(FetchHome fetchHome) {

        return new MainPresenterImp(view, context, fetchHome);

    }

}


