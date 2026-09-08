package network.util

enum class NetworkError {
    EMPTY,
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    SERVER_ERROR,
    UNKNOWN,
    BAD_REQUEST
}

fun Int.errorStatusHandle(): NetworkError {
    return when (this) {
        in 200..299 -> {
            NetworkError.EMPTY
        }
        400 -> NetworkError.BAD_REQUEST
        401 -> NetworkError.UNAUTHORIZED
        408 -> NetworkError.REQUEST_TIMEOUT
        409 -> NetworkError.CONFLICT
        429 -> NetworkError.TOO_MANY_REQUESTS
        in 500..599 -> NetworkError.SERVER_ERROR
        else -> NetworkError.UNKNOWN
    }
}

