package me.michaelhaas.triplist.di

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {
    @Provides
    fun provideApplication() = application

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)
}