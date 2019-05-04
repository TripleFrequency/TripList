package me.michaelhaas.triplist.service.core.model.resolver

typealias WebResolverCallback<T> = (suspend (T?) -> Unit)

abstract class BaseResolver<T : Any> {

    abstract suspend fun resolve(): T?
}