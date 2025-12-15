package com.sloth.proactify.data.remote.response

enum class NetworkError(val message: String) : Error {
    REQUEST_TIMEOUT("Error: Request Timeout"),
    UNAUTHORIZED("Error: Unauthorized"),
    CONFLICT("Error: Conflict"),
    TOO_MANY_REQUESTS("Error: Too Many Requests"),
    NO_INTERNET("Error: No Internet Connection"),
    PAYLOAD_TOO_LARGE("Error: Payload Too Large"),
    SERVER_ERROR("Error: Server Error"),
    SERIALIZATION("Error: Serialization Error"),
    UNKNOWN("Error: Unknown Error");

    override fun toString(): String {
        return message
    }
}