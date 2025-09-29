package cz.drekorian.avonmobilefetcher.productsapi

import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.settingsapi.Brochure
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.userAgent

internal class GetProductPagesUseCase(
//    TODO (marek.osvald): fix logging initialization (#226)
//    val httpClient: HttpClient,
) {
    suspend operator fun invoke(brochure: Brochure, sku: String): List<String> {
        val response = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "ce.avon.digital-catalogue.com"
                appendPathSegments("api", "v2", "products", brochure.productFeed)
            }

            val secret = getSecret(brochure = brochure, clientStorage = clientStorage)
            val token = getToken(key = secret)

            headers {
                userAgent(content = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:141.0) Gecko/20100101 Firefox/142.0")
                contentType(type = ContentType.parse(value = "application/json;charset=utf-8"))
                bearerAuth(token = token)
                header("Referer", BASE_HOST)
            }

            setBody(
                GetProductPagesRequest(
                    client = client,
                    environment = "view",
                    publication = brochure.externalId,
                    skus = listOf(sku),
                )
            )
        }

        return if (response.status == HttpStatusCode.OK) {
            return response.body<List<ProductDao>>().first().publications
                ?.filter { dao -> dao.publicationUid == brochure.externalId }
                ?.map { publicationDao -> publicationDao.page.toString() }
                ?.distinct()
                ?: listOf("")
        } else {
            logger.errorI18n("products_pages_response_error", brochure.id, sku, response.status.value)
            listOf("")
        }
    }
}
