package cz.drekorian.avonmobilefetcher.http

import cz.drekorian.avonmobilefetcher.OpenForTesting
import cz.drekorian.avonmobilefetcher.logger
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

/**
 * This class represents a single HTTP request.
 *
 * @see cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
 * @see cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
 * @see cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
 * @author Marek Osvald
 */
@OpenForTesting
abstract class Request {

    private val HttpResponse.isOk: Boolean
        get() = status == HttpStatusCode.OK

    /**
     * Returns true provided that given [HttpResponse] has not returned 200 OK status code. Otherwise, returns false
     * and prints the [errorMessage] to the [logger].
     *
     * @param response HTTP response
     * @param errorMessage error message to print when the response is not OK
     * @return true, provided that response has 200 OK status code, false otherwise
     */
    protected suspend fun checkStatusCode(response: HttpResponse, errorMessage: String): Boolean {
        if (!response.isOk) {
            val body = response.body<String>()
            logger.debug { "$errorMessage (${response.status}) $body" }
            return false
        }

        return true
    }
}
