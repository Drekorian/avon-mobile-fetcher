@file:JvmName("Main")

package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import mu.KLogger
import mu.KotlinLogging
import java.util.*

private const val I18N_RESOURCE_BUNDLE = "locale"

lateinit var logger : KLogger

fun main(args: Array<String>) {
    logger = KotlinLogging.logger("main")
    I18n.resourceBundle = ResourceBundle.getBundle(I18N_RESOURCE_BUNDLE, Locale.UK)
    MasterFlow().execute()
}