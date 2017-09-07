package com.location.home.app.di.modules;

import android.content.Context;

import com.location.home.app.di.scopes.MainScope;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.ui.presenters.MainPresenter;
import com.location.home.ui.presenters.MainPresenterImp;
import com.location.home.ui.views.MainView;

import dagger.Module;
import dagger.Provides;

@Module(includes = UseCasesModule.class)
public class MainPresenterModule {

    MainView view;
    Context context;

    public MainPresenterModule(MainView view, Context context) {

        this.view = view;
        this.context = context;

    }

    @Provides
    @MainScope
    public MainPresenter providePresenter(LocateHome locate) {

        return new MainPresenterImp(view, context, locate);

    }

}


