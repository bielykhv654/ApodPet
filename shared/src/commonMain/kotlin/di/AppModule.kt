package di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import photo_details_component.DefaultPhotoDetailsComponent
import photo_details_component.PhotoDetailsComponent
import main_tabs_component.DefaultMainTabsComponent
import main_tabs_component.MainTabsComponent
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import random_photos_component.RandomPhotosComponent
import random_photos_component.data.RandomPhotosRepository
import random_photos_component.data.RandomPhotosRepositoryImpl
import random_photos_component.integration.DefaultRandomPhotosComponent
import random_photos_component.integration.RandomPhotosStoreFactory
import date_range_component.RangePhotosComponent
import date_range_component.data.RangePhotosRepository
import date_range_component.data.RangePhotosRepositoryImpl
import date_range_component.integration.DefaultRangePhotosComponent
import date_range_component.integration.RangePhotosStoreFactory
import root_component.integration.DefaultRootComponent
import root_component.integration.RootComponent
import saved_photos_component.SavedPhotosComponent
import saved_photos_component.data.SavedPhotosRepository
import saved_photos_component.data.SavedPhotosRepositoryImpl
import saved_photos_component.integration.DefaultSavedPhotosComponent
import saved_photos_component.integration.SavedPhotosStoreFactory

val appModule = module {

    singleOf(::RandomPhotosRepositoryImpl) { bind<RandomPhotosRepository>() }
    singleOf(::RangePhotosRepositoryImpl) { bind<RangePhotosRepository>() }
    singleOf(::SavedPhotosRepositoryImpl) { bind<SavedPhotosRepository>() }

    singleOf(::RandomPhotosStoreFactory)
    single<RandomPhotosComponent.Factory> {
        DefaultRandomPhotosComponent.Factory(randomPhotosStoreFactory = get())
    }

    singleOf(::RangePhotosStoreFactory)
    single<RangePhotosComponent.Factory> {
        DefaultRangePhotosComponent.Factory(rangePhotosStoreFactory = get())
    }

    singleOf(::SavedPhotosStoreFactory)
    single<SavedPhotosComponent.Factory> {
        DefaultSavedPhotosComponent.Factory(savedPhotosStoreFactory = get())
    }

    single<PhotoDetailsComponent.Factory> {
        DefaultPhotoDetailsComponent.Factory()
    }

    single<MainTabsComponent.Factory> {
        DefaultMainTabsComponent.Factory(
            randomPhotosComponentFactory = get(),
            rangePhotosComponentFactory = get(),
            savedPhotosComponentFactory = get()
        )
    }

    single<RootComponent.Factory> {
        DefaultRootComponent.Factory(
            mainTabsComponentFactory = get(),
            photoDetailsComponentFactory = get()
        )
    }

    single<StoreFactory> {
        DefaultStoreFactory()
    }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule, coreModule
        )
    }
}
