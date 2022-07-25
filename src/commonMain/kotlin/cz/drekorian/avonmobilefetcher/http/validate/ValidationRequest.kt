package cz.drekorian.avonmobilefetcher.http.validate

import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.http.login.LoginRequest
import cz.drekorian.avonmobilefetcher.model.Campaign
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class ValidationRequest : Request() {

//    companion object {
//
//        private const val VALIDATION_URL = "https://apim-eu.api-prod.aws.avon.com/v1/CZ/CS/rep/%s/itemvalidation"
//    }

    suspend fun send(
        campaign: Campaign,
        lineNumbers: List<String>,
    ): List<ItemValidation> = KtorHttpClient.client.get {
        url {
            protocol = URLProtocol.HTTPS
            host = "apim-eu.api-prod.aws.avon.com"
            appendPathSegments("v1", "CZ", "CS", "rep", LoginRequest.USER_ID, "itemvalidation")
            with(parameters) {
                append("lineNrs", lineNumbers.map { lineNumber -> "$lineNumber:1" }.joinToString(separator = ","))
                append("cmpgnId", "${campaign.year}${campaign.id}")
                append("placOrdSctnCd", "REGULAR")
                append("custId", "0")
                append("ordNr", "")
                append("rule", "basicName")
            }
            header("x-sec-token", (KtorHttpClient.cookieJar["avn_tkn"]?.value ?: ""))
        }
    }.body()
}
