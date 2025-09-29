package cz.drekorian.avonmobilefetcher.productsapi

import com.appstractive.jwt.jwt
import com.appstractive.jwt.sign
import com.appstractive.jwt.signatures.hs256
import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.http.pagedata.PageDataRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.settingsapi.Brochure
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.delay
import okio.ByteString.Companion.toByteString
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class GetProductsUseCase(
//    TODO (marek.osvald): fix logging initialization (#226)
//    val httpClient: HttpClient,
    val pageDataRequest: PageDataRequest,
) {
    suspend operator fun invoke(
        brochure: Brochure,
    ): List<Product> {
        logger.infoI18n("products_request", brochure.id)
        val products = (1..3).flatMap {
            val response = sendRequest(brochure)
            if (response.status == HttpStatusCode.OK) {
                return@flatMap response.body<List<ProductDao>>().map(Product::fromDao)
            }

            if (it < 3) {
                logger.warn { "Products API has return ${response.status}, attempt no. ${it + 1} in 5 seconds" }
                delay(5_000L)
            }

            return@flatMap emptyList()
        }
//
//        val maxPage = brochure. products.maxOfOrNull { it. ?: 0 } ?: 0
//
//        (1..maxPage).forEach { page ->
//            logger.debugI18n("page_data_request", page, catalog.id)
//            val pageResponse = runBlocking { pageDataRequest.send(campaign, catalog, page) }
//            if (pageResponse == null) {
//                logger.errorI18n("page_data_response_null", page, catalog.id)
//                return@forEach
//            }
//
//            val foundIds = products.map { product -> product.id }
//            pageResponse.pageData.ids.forEach { id ->
//                if (id != INVALID_ID && id !in foundIds) {
//                    logger.debugI18n("page_data_new_product", id, catalog.id, page)
//                    products += cz.drekorian.avonmobilefetcher.model.Product.fromPageData(id, page)
//                }
//            }
//        }

        return products
    }

    private suspend fun sendRequest(brochure: Brochure): HttpResponse {
        val key = getSecret(brochure = brochure, clientStorage = clientStorage)
        val token = getToken(key)

        return httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "ce.avon.digital-catalogue.com"
                appendPathSegments("api", "v2", "products", brochure.productFeed, "indexes")
            }

            headers {
                userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:141.0) Gecko/20100101 Firefox/142.0")
                header("Accept-Language", "cs,sk;q=0.8,en-US;q=0.5,en;q=0.3")
                header("Origin", BASE_HOST)
                header("Sec-GPC", "1")
                header("Connection", "keep-alive")
                header("Sec-Fetch-Desc", "empty")
                header("Sec-Fetch-Mode", "no-cors")
                header("Sec-Fetch-Site", "cross-site")
                header("TE", "trailers")
                contentType(type = ContentType.parse(value = "application/json;charset=utf-8"))
                bearerAuth(token = token)
                header("Priority", "u=4")
                header("Pragma", "no-cache")
                header("Cache-Control", "no-cache")
                header("Referer", BASE_HOST)
            }

            setBody(
                GetProductsRequest(
                    index = "publication_products",
                    environment = "view",
                    client = client,
                    clientStorage = clientStorage,
                    publication = brochure.externalId,
                    columns = listOf(
                        "sku",
                        "concept_code",
                        "fsc",
                        "title",
                        "description",
                        "price",
                        "price_standard",
                        "has_perfect_corp",
                        "perfect_corp_model",
                        "product_order",
                        "perfect_corp_patterns",
                        "perfect_corp_patterns_text",
                        "is_look_1",
                        "is_look_2",
                        "is_look_3",
                        "predefined_variation_for_looks",
                        "condition",
                        "condition_description",
                        "applies_to",
                        "gift",
                        "offer_price",
                        "offer_discount",
                        "limit",
                        "group_1",
                        "group_2",
                        "group_3"
                    )
                )
            )
        }
    }

}

internal fun getSecret(brochure: Brochure, clientStorage: String): String {
    val secret = listOf(BASE_HOST, clientStorage, brochure.externalId).joinToString(separator = "|")
    return secret.toByteArray().toByteString().sha256().hex().lowercase()
}

@OptIn(ExperimentalTime::class)
internal suspend fun getToken(key: String): String = jwt {
    claims {
        issuer = "publication-view"
        audience = "products-api"
        issuedAt()
        expires()
        claim("key", key)
    }
}.sign { hs256 { secret = key.toByteArray() } }.toString()
