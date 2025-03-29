package cz.drekorian.avonmobilefetcher.multiplatform.util

import cz.drekorian.avonmobilefetcher.BuildConfig

internal actual fun getFetcherCommandName(): String = "avon-mobile-fetcher-${BuildConfig.appVersion}-mingwx64.exe"