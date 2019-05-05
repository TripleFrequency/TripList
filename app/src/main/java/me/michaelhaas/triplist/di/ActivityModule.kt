package me.michaelhaas.triplist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.michaelhaas.triplist.ui.*
import me.michaelhaas.triplist.ui.vm.AllTripsViewModel
import me.michaelhaas.triplist.ui.vm.TripDetailsViewModel
import me.michaelhaas.triplist.ui.vm.TripEditorViewModel
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

    @ContributesAndroidInjector
    abstract fun contributeToTripEditorFragment(): TripEditorFragment

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

    @Binds
    @IntoMap
    @ViewModelKey(TripEditorViewModel::class)
    internal abstract fun tripEditorViewModel(viewModel: TripEditorViewModel): ViewModel
}