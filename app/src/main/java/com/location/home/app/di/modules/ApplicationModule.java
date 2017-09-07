package com.location.home.app.di.modules;

import android.content.Context;

import com.location.home.app.HomeLocationApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

  private final HomeLocationApplication application;

  public ApplicationModule(HomeLocationApplication application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return this.application;
  }

}
