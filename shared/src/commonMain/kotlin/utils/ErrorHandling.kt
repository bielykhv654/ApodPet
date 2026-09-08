package utils

import apodpet.shared.generated.resources.Res
import apodpet.shared.generated.resources.demo_token_limit
import apodpet.shared.generated.resources.error_default
import apodpet.shared.generated.resources.error_serialization
import apodpet.shared.generated.resources.error_server
import apodpet.shared.generated.resources.error_unknown
import apodpet.shared.generated.resources.network_error
import org.jetbrains.compose.resources.StringResource

interface StringMessage {
    val message: StringResource
}

fun getErrorType(errorCode: Int): ErrorType {
    return when (errorCode) {

        -1 -> ErrorType.UNKNOWN_ERROR
        1 -> ErrorType.NO_INTERNET
        2 -> ErrorType.SERIALIZATION_ERROR
        429-> ErrorType.DEMO_TOKEN_LIMIT
        500 -> ErrorType.SERVER_ERROR
        else -> ErrorType.DEFAULT_ERROR
    }
}

enum class ErrorType : StringMessage {

    DEMO_TOKEN_LIMIT { override val message: StringResource = Res.string.demo_token_limit},
    SERIALIZATION_ERROR { override val message: StringResource = Res.string.error_serialization },
    SERVER_ERROR { override val message: StringResource = Res.string.error_server },
    UNKNOWN_ERROR { override val message: StringResource = Res.string.error_unknown },
    NO_INTERNET { override val message: StringResource = Res.string.network_error },
    DEFAULT_ERROR { override val message: StringResource = Res.string.error_default }
}
