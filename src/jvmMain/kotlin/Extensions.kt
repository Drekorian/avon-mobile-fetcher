@file:JvmName("ExtensionsJvm")
package cz.drekorian.avonmobilefetcher

actual fun String.nFormat(vararg args: String) = format(*args)
