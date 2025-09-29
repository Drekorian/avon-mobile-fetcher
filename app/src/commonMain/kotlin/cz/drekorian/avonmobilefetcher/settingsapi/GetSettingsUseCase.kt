package cz.drekorian.avonmobilefetcher.settingsapi

import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.productsapi.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path

internal class GetSettingsUseCase(
//    TODO (marek.osvald): fix logging initialization (#226)
//    private val httpClient: HttpClient,
) {

    suspend operator fun invoke(campaign: Campaign): List<Brochure> =
        httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                path("get-settings.php")
                with(parameters) {
                    append(name = "market", value = "CZ")
                    append(name = "campaign_id", value = "${campaign.id}_CZ_${campaign.year}")
                    append(name = "with_private", value = "true")
                }
            }
        }.body<BrochuresDto>().brochures.map(Brochure::fromDao)
}
