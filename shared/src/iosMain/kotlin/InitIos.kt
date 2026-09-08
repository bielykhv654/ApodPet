import di.appModule
import di.coreModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initialize(onKoinStart: KoinApplication.() -> Unit) {
    startKoin {
        onKoinStart()
        modules(
            appModule, coreModule
        )
    }
}