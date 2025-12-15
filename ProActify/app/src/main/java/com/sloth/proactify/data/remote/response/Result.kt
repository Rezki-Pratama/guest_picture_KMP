package com.sloth.proactify.data.remote.response

// Sealed interface for results with generics for success and error
sealed interface Results<out D, out E: Error> {
    data class Success<out D>(val data: D): Results<D, Nothing>
    data class Error<out E: com.sloth.proactify.data.remote.response.Error>(val error: E): Results<Nothing, E>
}

// Map function to transform success data while keeping error type intact
inline fun <T, E: Error, R> Results<T, E>.map(map: (T) -> R): Results<R, E> {
    return when(this) {
        is Results.Error -> Results.Error(error) // Keep the error as it is
        is Results.Success -> Results.Success(map(data)) // Transform the success data
    }
}

// Transform the result into an EmptyResult, useful for cases where only success/failure is needed
fun <T, E: Error> Results<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { Unit } // Explicitly return Unit for success
}

// Execute an action on success, returning the original result for chaining
inline fun <T, E: Error> Results<T, E>.onSuccess(action: (T) -> Unit): Results<T, E> {
    return when(this) {
        is Results.Error -> this // Do nothing on error, return as-is
        is Results.Success -> {
            action(data) // Execute the action on success data
            this // Return original result for chaining
        }
    }
}

// Execute an action on error, returning the original result for chaining
inline fun <T, E: Error> Results<T, E>.onError(action: (E) -> Unit): Results<T, E> {
    return when(this) {
        is Results.Error -> {
            action(error) // Execute action on the error
            this // Return original result for chaining
        }
        is Results.Success -> this // Do nothing on success, return as-is
    }
}

// TypeAlias for a result that has no data (empty) but includes error type
typealias EmptyResult<E> = Results<Unit, E>