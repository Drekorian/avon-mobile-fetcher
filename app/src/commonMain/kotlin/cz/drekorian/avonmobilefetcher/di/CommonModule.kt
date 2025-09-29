package cz.drekorian.avonmobilefetcher.di

import cz.drekorian.avonmobilefetcher.FetcherCommand
import cz.drekorian.avonmobilefetcher.domain.CampaignRepository
import cz.drekorian.avonmobilefetcher.domain.RuntimeOverride
import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.flow.ProductsFlow
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
import cz.drekorian.avonmobilefetcher.http.pagedata.PageDataRequest
import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
import cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
import cz.drekorian.avonmobilefetcher.productsapi.GetProductsRequest
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val commonModule = module {
    singleOf(::RuntimeOverride)

    factoryOf(::FetcherCommand)

    factoryOf(::CampaignRepository)

    factoryOf(::CatalogsFlow)
    factoryOf(::MasterFlow)
    factoryOf(::ProductsFlow)

    factoryOf(::CatalogsRequest)
    factoryOf(::PageDataRequest)
    factoryOf(::ProductDetailsRequest)
    factoryOf(::ProductsRequest)
}
