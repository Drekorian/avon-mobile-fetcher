package cz.drekorian.avonmobilefetcher

import com.github.ajalt.clikt.core.main
import cz.drekorian.avonmobilefetcher.di.commonModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

internal class Application: KoinComponent {
    fun start(args: Array<String>) {
        startKoin {
            modules(commonModule)
        }

        get<FetcherCommand>().main(args)
    }
}
