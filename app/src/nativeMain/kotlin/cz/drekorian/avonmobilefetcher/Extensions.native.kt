/**
 * This file contains various extensions.
 *
 * @author Marek Osvald
 */
@file:OptIn(ExperimentalForeignApi::class)

package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.multiplatform.CCharArray
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.sprintf

actual fun String.nFormat(vararg args: String): String = cFormat(*args)

internal fun String.cFormat(vararg args: String): String {
    val argsSize = args.size
    check(argsSize in (0..3)) {
        "Arguments size $argsSize out of range! Up to 3 formatting arguments are supported!"
    }

    val receiver = this
    lateinit var kString: String
    memScoped {
        val buffer = allocArray<ByteVar>(255)
        when (args.size) {
            0 -> return receiver
            1 -> receiver.cFormat(buffer, args[0])
            2 -> receiver.cFormat(buffer, args[0], args[1])
            else -> receiver.cFormat(buffer, args[0], args[1], args[2])
        }
        kString = buffer.toKString()
    }
    return kString
}

private fun String.cFormat(buffer: CCharArray, arg1: String) = sprintf(buffer, this, arg1)

private fun String.cFormat(buffer: CCharArray, arg1: String, arg2: String) = sprintf(buffer, this, arg1, arg2)

private fun String.cFormat(buffer: CCharArray, arg1: String, arg2: String, arg3: String) =
    sprintf(buffer, this, arg1, arg2, arg3)
