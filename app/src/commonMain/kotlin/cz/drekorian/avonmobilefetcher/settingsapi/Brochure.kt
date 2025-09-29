package cz.drekorian.avonmobilefetcher.settingsapi

internal data class Brochure(
    val id: Long,
    val externalId: String,
    val productFeed: String,
) {

    companion object {

        fun fromDao(dao: BrochureDto) = with(dao) {
            Brochure(
                id = id,
                externalId = externalId,
                productFeed = infos.publication.options.modules.feed.productFeed,
            )
        }
    }
}
