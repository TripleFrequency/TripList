package me.michaelhaas.triplist.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param.*
import com.google.firebase.analytics.FirebaseAnalytics.Event.*

sealed class AnalyticsBuilder(
    private val eventName: String,
    private val bundle: Bundle
) {

    fun log(firebaseAnalytics: FirebaseAnalytics) = firebaseAnalytics.logEvent(eventName, bundle)

    sealed class EndpointCalledEvent(endpoint: Endpoint, param: Int? = null) :
        AnalyticsBuilder(EVENT_NETWORK_CALL, Bundle().also {
            it.putString(ITEM_NAME, endpoint.logName)
            if (param != null) {
                it.putString(ITEM_ID, param.toString())
            }
        }) {
        enum class Endpoint(val logName: String) {
            TRIPS_ALL("trips_all"),
            TRIPS_DETAIL("trips_detail")
        }

        class AllTripsCalledEvent : EndpointCalledEvent(Endpoint.TRIPS_ALL)
        class TripDetailsCalledEvent(id: Int) : EndpointCalledEvent(Endpoint.TRIPS_DETAIL, id)

        private companion object {
            const val EVENT_NETWORK_CALL = "network_call"
        }
    }

    sealed class UserTripEvent(action: UserTripAction, tripId: Int) :
        AnalyticsBuilder(EVENT_USER_TRIP, Bundle().also {
            it.putString(ITEM_NAME, action.logName)
            it.putString(ITEM_ID, tripId.toString())
        }) {
        enum class UserTripAction(val logName: String) {
            TRIP_CREATED("trip_created"),
            TRIP_UPDATED("trip_updated"),
            TRIP_DELETED("trip_deleted")
        }

        class UserTripCreatedEvent(tripId: Int) : UserTripEvent(UserTripAction.TRIP_CREATED, tripId)
        class UserTripUpdatedEvent(tripId: Int) : UserTripEvent(UserTripAction.TRIP_UPDATED, tripId)
        class UserTripDeletedEvent(tripId: Int) : UserTripEvent(UserTripAction.TRIP_DELETED, tripId)

        private companion object {
            const val EVENT_USER_TRIP = "trip_user"
        }
    }

    sealed class TripEditorEvent(action: TripEditorAction, tripId: Int) :
        AnalyticsBuilder(EVENT_EDITOR_INTERACTION, Bundle().also {
            it.putString(ITEM_NAME, action.logName)
            it.putString(ITEM_ID, tripId.toString())
        }) {
        enum class TripEditorAction(val logName: String) {
            EDITOR_OPENED("trip_editor_opened"),
            EDITOR_DELETE_CANCELLED("trip_editor_delete_cancelled")
        }

        class TripEditorOpenedEvent(tripId: Int) : TripEditorEvent(TripEditorAction.EDITOR_OPENED, tripId)
        class TripEditorDeleteCancelledEvent(tripId: Int) :
            TripEditorEvent(TripEditorAction.EDITOR_DELETE_CANCELLED, tripId)

        private companion object {
            const val EVENT_EDITOR_INTERACTION = "trip_editor_interaction"
        }
    }
}