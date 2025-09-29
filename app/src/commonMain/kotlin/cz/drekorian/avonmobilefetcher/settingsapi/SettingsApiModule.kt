package cz.drekorian.avonmobilefetcher.settingsapi

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsApiModule = module {

    factoryOf(::GetSettingsUseCase)
}
