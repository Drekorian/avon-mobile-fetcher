package cz.drekorian.avonmobilefetcher.http.login

import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.http.validate.LoginRequestBody
import io.ktor.client.call.body

class LoginRequest : Request() {

    companion object {

        private const val LOGIN_URL = "https://www2.avoncosmetics.cz/cz-home/api/mab/secure/access"
        const val USER_ID = "803590990"
        const val PASSWORD = "06mo07"
    }

    suspend fun send(): LoginResponse = KtorHttpClient.post(
        url = LOGIN_URL,
        body = LoginRequestBody(
            userId = USER_ID,
            password = PASSWORD,
        ),
    ).body()
}
