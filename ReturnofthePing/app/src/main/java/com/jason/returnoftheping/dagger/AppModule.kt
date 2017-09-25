package com.jason.returnoftheping.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Jason on 9/24/17.
 */
@Module
class AppModule(var application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application
}