package cz.drekorian.avonmobilefetcher.http

import cz.drekorian.avonmobilefetcher.logger
import khttp.responses.Response

/**
 * This class represents a single HTTP request.
 *
 * @see cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
 * @see cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
 * @see cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
 * @author Marek Osvald
 */
abstract class Request {

    private val Response.isOk: Boolean
        get() = statusCode == 200

    /**
     * Returns true provided that given [Response] has not returned 200 OK status code. Otherwise returns false
     * and prints the [errorMessage] to the [logger].
     *
     * @param response HTTP response
     * @param errorMessage error message to print when the response is not OK
     * @return true, provided that response has 200 OK status code, false otherwise
     */
    protected fun checkStatusCode(response: Response, errorMessage: String): Boolean {
        if (!response.isOk) {
            logger.error("$errorMessage (${response.statusCode}) ${response.text}")
            return false
        }

        return true
    }
}
