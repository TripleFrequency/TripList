package me.michaelhaas.triplist.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {
    @Provides
    fun provideApplication() = application

    @Provides
    fun provideContext(app: Application) = app.applicationContext
}