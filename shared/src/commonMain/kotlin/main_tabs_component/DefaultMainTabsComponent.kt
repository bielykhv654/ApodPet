package main_tabs_component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import utils.NasaPhotoUiModel
import random_photos_component.RandomPhotosComponent
import date_range_component.RangePhotosComponent
import saved_photos_component.SavedPhotosComponent

class DefaultMainTabsComponent(
    componentContext: ComponentContext,
    randomPhotosComponentFactory: RandomPhotosComponent.Factory,
    rangePhotosComponentFactory: RangePhotosComponent.Factory,
    savedPhotosComponentFactory: SavedPhotosComponent.Factory,
    private val onOpenPhotoDetailsAction: (NasaPhotoUiModel) -> Unit
) : MainTabsComponent, ComponentContext by componentContext {

    override val randomPhotosComponent: RandomPhotosComponent =
        randomPhotosComponentFactory(childContext("RandomPhotosTab"))

    override val rangePhotosComponent: RangePhotosComponent =
        rangePhotosComponentFactory(childContext("RangePhotosTab"))

    override val savedPhotosComponent: SavedPhotosComponent =
        savedPhotosComponentFactory(childContext("SavedPhotosTab"))

    private val _selectedTab = MutableValue(0)
    override val selectedTab: Value<Int> = _selectedTab

    override fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    override fun openPhotoDetails(photo: NasaPhotoUiModel) {
        onOpenPhotoDetailsAction(photo)
    }

    class Factory(
        private val randomPhotosComponentFactory: RandomPhotosComponent.Factory,
        private val rangePhotosComponentFactory: RangePhotosComponent.Factory,
        private val savedPhotosComponentFactory: SavedPhotosComponent.Factory
    ) : MainTabsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onOpenPhotoDetails: (NasaPhotoUiModel) -> Unit
        ): MainTabsComponent {
            return DefaultMainTabsComponent(
                componentContext = componentContext,
                randomPhotosComponentFactory = randomPhotosComponentFactory,
                rangePhotosComponentFactory = rangePhotosComponentFactory,
                savedPhotosComponentFactory = savedPhotosComponentFactory,
                onOpenPhotoDetailsAction = onOpenPhotoDetails
            )
        }
    }
}
