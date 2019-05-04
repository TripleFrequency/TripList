package me.michaelhaas.triplist.di

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import me.michaelhaas.triplist.service.db.di.DatabaseModule
import me.michaelhaas.triplist.service.http.di.HttpModule

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        HttpModule::class
    ]
)
interface AppComponent {
    fun inject(app: Application)
}