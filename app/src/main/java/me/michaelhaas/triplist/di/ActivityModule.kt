package me.michaelhaas.triplist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.michaelhaas.triplist.ui.AllTripsFragment
import me.michaelhaas.triplist.ui.MainActivity
import me.michaelhaas.triplist.ui.TripDetailsActivity
import me.michaelhaas.triplist.ui.UserTripsFragment
import me.michaelhaas.triplist.ui.vm.AllTripsViewModel
import me.michaelhaas.triplist.ui.vm.TripDetailsViewModel
import me.michaelhaas.triplist.ui.vm.UserTripsViewModel

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeToMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeToDetailsActivity(): TripDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeToUserTripsFragment(): UserTripsFragment

    @ContributesAndroidInjector
    abstract fun contributeToAllTripsFragment(): AllTripsFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserTripsViewModel::class)
    internal abstract fun userTripsViewModel(viewModel: UserTripsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllTripsViewModel::class)
    internal abstract fun allTripsViewModel(viewModel: AllTripsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TripDetailsViewModel::class)
    internal abstract fun tripDetailsViewModel(viewModel: TripDetailsViewModel): ViewModel
}