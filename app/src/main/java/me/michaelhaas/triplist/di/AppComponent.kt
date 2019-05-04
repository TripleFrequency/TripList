package me.michaelhaas.triplist.di

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import me.michaelhaas.triplist.MyApplication
import me.michaelhaas.triplist.service.db.di.DatabaseModule
import me.michaelhaas.triplist.service.http.di.HttpModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        HttpModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    fun inject(app: Application)
}