package me.michaelhaas.triplist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.michaelhaas.triplist.ui.MainActivity
import me.michaelhaas.triplist.ui.UserTripsFragment
import me.michaelhaas.triplist.ui.vm.UserTripsViewModel

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeToMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeToUserTripsFragment(): UserTripsFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserTripsViewModel::class)
    internal abstract fun userTripsViewModel(viewModel: UserTripsViewModel): ViewModel
}