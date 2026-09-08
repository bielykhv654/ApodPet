package main_tabs_component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import utils.NasaPhotoUiModel
import random_photos_component.RandomPhotosComponent
import date_range_component.RangePhotosComponent
import saved_photos_component.SavedPhotosComponent

interface MainTabsComponent {
    val randomPhotosComponent: RandomPhotosComponent
    val rangePhotosComponent: RangePhotosComponent
    val savedPhotosComponent: SavedPhotosComponent

    val selectedTab: Value<Int>
    fun selectTab(tabIndex: Int)
    fun openPhotoDetails(photo: NasaPhotoUiModel)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onOpenPhotoDetails: (NasaPhotoUiModel) -> Unit
        ): MainTabsComponent
    }
}
