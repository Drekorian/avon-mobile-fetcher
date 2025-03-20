package cz.drekorian.avonmobilefetcher.multiplatform.util

import cz.drekorian.avonmobilefetcher.BuildConfig

internal actual fun getFetcherCommandName(): String = "java -jar avon-mobile-fetcher-all-${BuildConfig.appVersion}.jar"