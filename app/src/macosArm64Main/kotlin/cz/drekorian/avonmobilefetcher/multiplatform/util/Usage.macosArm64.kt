package cz.drekorian.avonmobilefetcher.multiplatform.util

import cz.drekorian.avonmobilefetcher.BuildConfig

@Suppress("SpellCheckingInspection")
internal actual fun getFetcherCommandName(): String = "avon-mobile-fetcher-${BuildConfig.appVersion}-macos-arm64.kexe"
