package com.sloth.proactify.data.remote.response

enum class DatabaseError(val message: String) : Error {
    CONFLICT("Error: Conflict"),
    TOO_MANY_REQUESTS("Error: Too Many Requests"),
    SERIALIZATION("Error: Serialization Error"),
    UNKNOWN("Error: Unknown Error");

    override fun toString(): String {
        return message
    }
}