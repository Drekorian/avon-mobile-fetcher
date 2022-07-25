package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.hostIsIp
import io.ktor.http.isSecure
import io.ktor.util.date.GMTDate
import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.min

/**
 * [CookiesStorage] that stores all the cookies in an in-memory map.
 */
@Suppress("DEPRECATION")
class CookieJar : CookiesStorage {
    private val container: MutableList<Cookie> = mutableListOf()
    private val oldestCookie: AtomicLong = atomic(0L)
    private val mutex = Mutex()

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        println("GET $requestUrl")
        val date = GMTDate()
        if (date.timestamp >= oldestCookie.value) cleanup(date.timestamp)

        return@withLock container.filter { it.matches(requestUrl) }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        with(cookie) {
            if (name.isBlank()) return@withLock
        }

        container.removeAll { it.name == cookie.name && it.matches(requestUrl) }
        container.add(cookie.fillDefaults(requestUrl))
        cookie.expires?.timestamp?.let { expires ->
            if (oldestCookie.value > expires) {
                oldestCookie.value = expires
            }
        }
    }

    override fun close() {
    }

    operator fun get(name: String): Cookie? = container.find { cookie -> cookie.name == name }

    fun hasCookie(name: String): Boolean = this[name] != null

    private fun cleanup(timestamp: Long) {
        container.removeAll { cookie ->
            val expires = cookie.expires?.timestamp ?: return@removeAll false
            expires < timestamp
        }

        val newOldest = container.fold(Long.MAX_VALUE) { acc, cookie ->
            cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
        }

        oldestCookie.value = newOldest
    }
}

private fun Cookie.matches(requestUrl: Url): Boolean {
    val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
        ?: error("Domain field should have the default value")

    val path = with(path) {
        val current = path ?: error("Path field should have the default value")
        if (current.endsWith('/')) current else "$path/"
    }

    val host = requestUrl.host.toLowerCasePreservingASCIIRules()
    val requestPath = let {
        val pathInRequest = requestUrl.encodedPath
        if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
    }

    if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
        return false
    }

    if (path != "/" &&
        requestPath != path &&
        !requestPath.startsWith(path)
    ) return false

    return !(secure && !requestUrl.protocol.isSecure())
}

private fun Cookie.fillDefaults(requestUrl: Url): Cookie {
    var result = this

    if (result.path?.startsWith("/") != true) {
        result = result.copy(path = requestUrl.encodedPath)
    }

    if (result.domain.isNullOrBlank()) {
        result = result.copy(domain = requestUrl.host)
    }

    return result
}
