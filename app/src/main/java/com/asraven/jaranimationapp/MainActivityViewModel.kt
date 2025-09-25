package com.asraven.jaranimationapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : ViewModel() {


    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(
        MainActivityUiState(      listOf(
            CardData(1, "Adventure Awaits", "Discover breathtaking landscapes and hidden gems around the world. From mountain peaks to ocean depths, every journey tells a story worth sharing.", android.R.drawable.ic_menu_camera),
            CardData(2, "Culinary Delights", "Explore flavors from different cultures and master the art of cooking. Each recipe is a gateway to understanding traditions and creating memorable experiences.", android.R.drawable.ic_menu_gallery),
            CardData(3, "Tech Innovation", "Stay ahead with the latest technology trends and breakthroughs. Innovation shapes our future and transforms the way we live, work, and connect.", android.R.drawable.ic_menu_info_details),
            CardData(4, "Wellness Journey", "Embrace a healthier lifestyle with mindful practices and balanced living. Your well-being is the foundation for achieving all your dreams and aspirations.", android.R.drawable.ic_menu_mapmode),
            CardData(5, "Creative Arts", "Express yourself through various forms of artistic creation. Art has the power to inspire, heal, and bring people together across all boundaries.", android.R.drawable.ic_menu_search)
        )),
    )
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()


}



data class MainActivityUiState(
    val cards : List<CardData> = listOf()
)

data class CardData(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: Int
)